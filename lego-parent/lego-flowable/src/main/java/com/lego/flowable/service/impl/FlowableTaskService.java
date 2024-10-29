package com.lego.flowable.service.impl;

import cn.hutool.extra.servlet.ServletUtil;
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
import com.lego.flowable.assembler.FlowableCommentAssembler;
import com.lego.flowable.assembler.FlowableModelAssembler;
import com.lego.flowable.assembler.FlowableTaskAssembler;
import com.lego.flowable.dto.FlowableCommentInfo;
import com.lego.flowable.dto.FlowableTaskFormDetailInfo;
import com.lego.flowable.dto.FlowableTaskInfo;
import com.lego.flowable.mapper.FlowableCommentMapper;
import com.lego.flowable.service.IFlowableTaskService;
import com.lego.flowable.vo.FLowbaleTaskClaimVO;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.flowable.vo.FlowableTaskDelegateVO;
import com.lego.flowable.vo.FlowableTaskLogType;
import com.lego.flowable.vo.FlowableTaskRejectVO;
import com.lego.flowable.vo.FlowableTaskSearchVO;
import com.lego.flowable.vo.FlowableTaskTransferVO;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.api.history.HistoricTaskLogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FlowableTaskService extends FlowableService<FlowableTaskAssembler> implements IFlowableTaskService {

    @Autowired
    private ISysEmployeeDao employeeDao;

    @Autowired
    private FlowableCommentMapper commentMapper;

    @Autowired
    private FlowableModelAssembler modelAssembler;

    @Autowired
    private FlowableCommentAssembler commentAssembler;

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

        String logType = FlowableTaskLogType.REJECT.getCode();
        List<Comment> comments = commentMapper.selectCommentsByTaskId(taskId);
        List<FlowableCommentInfo> commentInfos = commentAssembler.create(comments);
        if (!commentInfos.stream().anyMatch(FlowableCommentInfo::isReject)) {
            List<HistoricTaskLogEntry> taskLogs = historyService.createHistoricTaskLogEntryQuery()
                .taskId(taskId).type(logType).list();
            commentInfos.addAll(commentAssembler.createByLog(taskLogs));
        }
        commentInfos = commentInfos.stream()
            .sorted(Comparator.comparing(FlowableCommentInfo::getCreateTime, Comparator.naturalOrder()))
            .collect(Collectors.toList());

        FlowableTaskFormDetailInfo detailInfo = new FlowableTaskFormDetailInfo(task.getId(), task.getName(), code);
        detailInfo.setVariables(taskLocalVariables);
        detailInfo.setFormKey(formKey);
        detailInfo.setFinished(task instanceof HistoricTaskInstance);
        detailInfo.setComments(commentInfos);
        detailInfo.setInstanceId(task.getProcessInstanceId());
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

    @Override
    public void downloadImage(HttpServletResponse response, String taskId) {
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
        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
            .executionId(task.getExecutionId())
            .list();
        List<String> finishedTaskSet = new ArrayList<>();
        List<String> finishedSequenceFlowSet = new ArrayList<>();
        // 查询所有已完成的元素
        List<HistoricActivityInstance> finishedElementList = activities.stream().collect(Collectors.toList());
        finishedElementList.forEach(item -> {
            if (BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW.equals(item.getActivityType())
                || BpmnXMLConstants.ELEMENT_FLOW_CONDITION.equals(item.getActivityType())) {
                finishedSequenceFlowSet.add(item.getActivityId());
            } else {
                finishedTaskSet.add(item.getActivityId());
            }
        });
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engineConfig.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", finishedTaskSet, finishedSequenceFlowSet, engineConfig.getActivityFontName(), engineConfig.getLabelFontName(), engineConfig.getAnnotationFontName(), engineConfig.getClassLoader(), 1.0, true);
        ServletUtil.write(response, in);
    }

}
