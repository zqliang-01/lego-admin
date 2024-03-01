package com.lego.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.lego.core.common.Constants;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.flowable.FlowableService;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.flowable.assembler.FlowableModelAssembler;
import com.lego.flowable.assembler.FlowableTaskAssembler;
import com.lego.flowable.dto.FlowableTaskFormDetailInfo;
import com.lego.flowable.dto.FlowableTaskInfo;
import com.lego.flowable.service.IFlowableTaskService;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.flowable.vo.FlowableTaskSearchVO;
import com.lego.flowable.vo.ProcessConstants;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            .orderByTaskCreateTime().desc();
        if (!employee.containRole(Constants.ADMIN_ROLE)) {
            taskQuery.taskCandidateOrAssigned(employeeCode).taskCandidateGroupIn(candidateGroups);
        }
        if (StringUtil.isNotBlank(vo.getInstanceId())) {
            taskQuery.processInstanceId(vo.getInstanceId());
        }
        LegoPage<Task> page = createPage(taskQuery, vo, Task.class);
        return assembler.create(page);
    }

    @Override
    public LegoPage<FlowableTaskInfo> findCompletedBy(String employeeCode, FlowableTaskSearchVO vo) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
            .finished()
            .orderByHistoricTaskInstanceEndTime()
            .desc();
        if (!employee.isAdmin()) {
            List<String> candidateGroups = EntityUtil.getCode(employee.getRoles());
            candidateGroups.add(EntityUtil.getCode(employee.getDept()));
            taskInstanceQuery.or()
                .taskAssignee(employeeCode)
                .taskCandidateGroupIn(candidateGroups)
                .endOr();
        }
        if (StringUtil.isNotBlank(vo.getInstanceId())) {
            taskInstanceQuery.processInstanceId(vo.getInstanceId());
        }
        LegoPage<HistoricTaskInstance> page = createPage(taskInstanceQuery, vo, HistoricTaskInstance.class);
        return assembler.createHis(page);
    }

    @Override
    public void complete(String employeeCode, FlowableTaskCompleteVO vo) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        TaskQuery taskQuery = taskService.createTaskQuery().active().taskId(vo.getId());
        if (!employee.isAdmin()) {
            List<String> candidateGroups = EntityUtil.getCode(employee.getRoles());
            candidateGroups.add(EntityUtil.getCode(employee.getDept()));
            taskQuery.taskCandidateOrAssigned(employeeCode).taskCandidateGroupIn(candidateGroups);
        }
        Task task = taskQuery.singleResult();
        BusinessException.check(task != null, "当前任务审核人非[{0}]，审核失败！", employeeCode);

        if (StringUtil.isNotBlank(task.getFormKey())) {
            BusinessException.check(!vo.getVariables().isEmpty(), "任务完工失败，请填写表单信息！");
        }

        taskService.setAssignee(vo.getId(), employeeCode);
        if (vo.getVariables().isEmpty()) {
            taskService.complete(vo.getId());
            return;
        }
        String code = vo.getStringValue(ProcessConstants.FORM_UNIQUE_KEY);
        taskService.setVariableLocal(vo.getId(), ProcessConstants.FORM_UNIQUE_KEY, code);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        String localScopeValue = modelAssembler.getUserTaskAttributeValue(bpmnModel, task.getTaskDefinitionKey(), ProcessConstants.FORM_LOCAL_SCOPE);
        boolean localScope = Convert.toBool(localScopeValue, false);
        taskService.complete(vo.getId(), vo.getVariables(), localScope);
    }

    @Override
    public void reject(String employeeCode, FlowableTaskCompleteVO vo) {
        Task task = taskService.createTaskQuery().taskId(vo.getId()).singleResult();
        BusinessException.check(task != null, "不存在的任务信息[{0}]，任务拒绝失败！", vo.getId());
        BusinessException.check(!task.isSuspended(), "任务处于挂起状态，任务拒绝失败！");
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(task.getProcessDefinitionId())
            .singleResult();
        // 构建查询条件
        List<HistoricActivityInstance> allActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(task.getProcessInstanceId())
            .list();
        if (CollUtil.isEmpty(allActivityInstanceList)) {
            return;
        }
        List<String> finishedTaskIds = new ArrayList<>();
        for (HistoricActivityInstance item : allActivityInstanceList) {
            if (item.getEndTime() == null) {
                continue;
            }
            if (BpmnXMLConstants.ELEMENT_TASK_USER.equals(item.getActivityType())
                || BpmnXMLConstants.ELEMENT_EVENT_START.equals(item.getActivityType())) {
                finishedTaskIds.add(item.getActivityId());
            }
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        UserTask element = modelAssembler.getUserTaskByKey(bpmnModel, task.getTaskDefinitionKey());
        List<String> taskIds = modelAssembler.getBeforeUserTaskId(element, finishedTaskIds);
        for (String taskId : taskIds) {
            // 添加审批意见
            if (StringUtil.isNotBlank(vo.getComment())) {
                taskService.addComment(taskId, task.getProcessInstanceId(), vo.getComment());
            }
        }
        if (taskIds.size() == 1) {
            List<Execution> executions = runtimeService.createExecutionQuery().parentId(task.getProcessInstanceId()).list();
            List<String> executionIds = executions.stream().map(Execution::getId).collect(Collectors.toList());
            runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveExecutionsToSingleActivityId(executionIds, taskIds.get(0))
                .changeState();
            return;
        }
        runtimeService.createChangeActivityStateBuilder()
            .processInstanceId(task.getProcessInstanceId())
            .moveSingleActivityIdToActivityIds(task.getTaskDefinitionKey(), taskIds)
            .changeState();
    }

    @Override
    public FlowableTaskFormDetailInfo findCodeVariableBy(String taskId) {
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery()
            .includeTaskLocalVariables()
            .finished()
            .taskId(taskId)
            .singleResult();
        if (task != null) {
            String formKey = task.getFormKey();
            Object value = task.getTaskLocalVariables().get(ProcessConstants.FORM_UNIQUE_KEY);
            String code = StringUtil.toString(value);
            return new FlowableTaskFormDetailInfo(task.getId(), formKey, code);
        }
        return null;
    }

    @Override
    public List<FlowableTaskInfo> findBy(String instanceId, String key) {
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(instanceId)
            .taskDefinitionKey(key)
            .orderByHistoricTaskInstanceEndTime()
            .asc()
            .list();
        return assembler.createHis(tasks);
    }

}
