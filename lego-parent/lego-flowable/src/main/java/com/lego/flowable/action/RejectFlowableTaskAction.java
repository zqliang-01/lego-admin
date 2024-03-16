package com.lego.flowable.action;

import cn.hutool.core.collection.CollUtil;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.flowable.vo.FlowableTaskRejectVO;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RejectFlowableTaskAction extends FlowableTaskAction {

    private FlowableTaskRejectVO vo;

    public RejectFlowableTaskAction(String operatorCode, FlowableTaskRejectVO vo) {
        super(SysPermissionCode.oaUndo, operatorCode, vo.getId());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        super.preprocess();
        BusinessException.check(!task.isSuspended(), "任务处于挂起状态，任务拒绝失败！");
    }

    @Override
    protected void doRun() {
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
        // 添加审批意见
        if (StringUtil.isNotBlank(vo.getComment())) {
            taskService.addComment(vo.getId(), task.getProcessInstanceId(), vo.getComment());
        }
        taskService.setAssignee(vo.getId(), operatorCode);
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
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }
}
