package com.lego.flowable.service.impl;

import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.flowable.FlowableProcessConstants;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.flowable.action.CompleteFlowableTaskAction;
import com.lego.flowable.action.DelegateFlowableTaskAction;
import com.lego.flowable.action.RejectFlowableTaskAction;
import com.lego.flowable.action.SaveFlowableTaskAction;
import com.lego.flowable.action.TransferFlowableTaskAction;
import com.lego.flowable.assembler.FlowableModelAssembler;
import com.lego.flowable.assembler.FlowableTaskAssembler;
import com.lego.flowable.dto.FlowableTaskFormDetailInfo;
import com.lego.flowable.dto.FlowableTaskInfo;
import com.lego.flowable.service.IFlowableTaskService;
import com.lego.flowable.vo.FLowbaleTaskClaimVO;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.flowable.vo.FlowableTaskDelegateVO;
import com.lego.flowable.vo.FlowableTaskRejectVO;
import com.lego.flowable.vo.FlowableTaskSearchVO;
import com.lego.flowable.vo.FlowableTaskTransferVO;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FlowableTaskService extends FlowableService<FlowableTaskAssembler> implements IFlowableTaskService {

    @Autowired
    private ISysEmployeeDao employeeDao;

    @Autowired
    private FlowableModelAssembler modelAssembler;

    @Override
    public LegoPage<FlowableTaskInfo> findUndoBy(String employeeCode, FlowableTaskSearchVO vo) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        List<String> candidateGroups = EntityUtil.getCode(employee.getRoles());
        candidateGroups.add(EntityUtil.getCode(employee.getDept()));
        TaskQuery taskQuery = taskService.createTaskQuery()
            .active()
            .taskCandidateOrAssigned(employeeCode)
            .taskCandidateGroupIn(candidateGroups)
            .orderByTaskCreateTime().desc();
        if (StringUtil.isNotBlank(vo.getName())) {
            taskQuery.taskNameLike("%" + vo.getName() + "%");
        }
        LegoPage<Task> page = createPage(taskQuery, vo, Task.class);
        return assembler.create(page);
    }

    @Override
    public LegoPage<FlowableTaskInfo> findCompletedBy(String employeeCode, FlowableTaskSearchVO vo) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
            .finished()
            .taskAssignee(employeeCode)
            .orderByHistoricTaskInstanceEndTime().desc();
        if (StringUtil.isNotBlank(vo.getName())) {
            taskInstanceQuery.taskNameLike("%" + vo.getName() + "%");
        }
        LegoPage<HistoricTaskInstance> page = createPage(taskInstanceQuery, vo, HistoricTaskInstance.class);
        return assembler.createHis(page);
    }

    @Override
    public LegoPage<FlowableTaskInfo> findClaimdBy(String employeeCode, FlowableTaskSearchVO vo) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        List<String> candidateGroups = EntityUtil.getCode(employee.getRoles());
        candidateGroups.add(EntityUtil.getCode(employee.getDept()));
        TaskQuery taskQuery = taskService.createTaskQuery()
            .active()
            .taskCandidateUser(employeeCode)
            .taskCandidateGroupIn(candidateGroups)
            .orderByTaskCreateTime().desc();
        if (StringUtil.isNotBlank(vo.getName())) {
            taskQuery.taskNameLike("%" + vo.getName() + "%");
        }
        LegoPage<Task> page = createPage(taskQuery, vo, Task.class);
        return assembler.create(page);
    }

    @Override
    public FlowableTaskFormDetailInfo findCodeVariableBy(String taskId) {
        TaskInfo task = historyService.createHistoricTaskInstanceQuery()
            .includeTaskLocalVariables()
            .finished()
            .taskId(taskId)
            .singleResult();
        if (task == null) {
            task = taskService.createTaskQuery()
                .includeTaskLocalVariables()
                .taskId(taskId)
                .singleResult();
        }
        BusinessException.check(task != null, "不存在的任务ID[{0}]，查询任务明细失败！", taskId);

        String formKey = task.getFormKey();
        Map<String, Object> taskLocalVariables = task.getTaskLocalVariables();
        String code = StringUtil.toString(taskLocalVariables.get(FlowableProcessConstants.FORM_UNIQUE_KEY));
        List<Comment> comments = taskService.getTaskComments(taskId);
        String commentMsg = comments.stream()
            .map(Comment::getFullMessage)
            .reduce((s1, s2) -> s1 + " " + s2)
            .orElse("");
        FlowableTaskFormDetailInfo detailInfo = new FlowableTaskFormDetailInfo(task.getId(), task.getName(), code);
        detailInfo.setVariables(taskLocalVariables);
        detailInfo.setComment(commentMsg);
        detailInfo.setFormKey(formKey);
        detailInfo.setFinished(task instanceof HistoricTaskInstance);
        return detailInfo;
    }

    @Override
    public List<FlowableTaskInfo> findBy(String instanceId, String key) {
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(instanceId)
            .taskDefinitionKey(key)
            .orderByHistoricTaskInstanceStartTime()
            .asc()
            .list();
        return assembler.createHis(tasks);
    }

    @Override
    public void complete(String employeeCode, FlowableTaskCompleteVO vo) {
        new CompleteFlowableTaskAction(employeeCode, vo).run();
    }

    @Override
    public void save(String employeeCode, FlowableTaskCompleteVO vo) {
        new SaveFlowableTaskAction(employeeCode, vo).run();
    }

    @Override
    public void reject(String employeeCode, FlowableTaskRejectVO vo) {
        new RejectFlowableTaskAction(employeeCode, vo).run();
    }

    @Override
    public void delegate(String employeeCode, FlowableTaskDelegateVO vo) {
        new DelegateFlowableTaskAction(employeeCode, vo).run();
    }

    @Override
    public void claim(String loginCode, FLowbaleTaskClaimVO vo) {
        taskService.claim(vo.getId(), loginCode);
    }

    @Override
    public void unClaim(String loginCode, FLowbaleTaskClaimVO vo) {
        taskService.unclaim(vo.getId());
    }

    @Override
    public void transfer(String loginCode, FlowableTaskTransferVO vo) {
        new TransferFlowableTaskAction(loginCode, vo).run();
    }

}
