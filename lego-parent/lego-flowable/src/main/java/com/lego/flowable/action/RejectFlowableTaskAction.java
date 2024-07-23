package com.lego.flowable.action;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.flowable.vo.FlowableCommentType;
import com.lego.flowable.vo.FlowableTaskLogType;
import com.lego.flowable.vo.FlowableTaskRejectVO;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskLogEntry;
import org.flowable.task.api.history.HistoricTaskLogEntryQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        String processInstanceId = task.getProcessInstanceId();
        if (StringUtil.isNotBlank(vo.getComment())) {
            taskService.addComment(vo.getId(), processInstanceId, FlowableCommentType.REJECT.getCode(), vo.getComment());
        }
        taskService.setAssignee(vo.getId(), operatorCode);
        List<String> finishedTaskIds = getFinishedTaskIds();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        UserTask element = modelAssembler.getUserTaskByKey(bpmnModel, task.getTaskDefinitionKey());
        List<String> beforeTaskIds = modelAssembler.getBeforeUserTaskId(element, finishedTaskIds);
        BusinessException.check(CollectionUtil.isNotEmpty(beforeTaskIds), "当前任务节点为起始任务节点，无法拒绝任务！");

        beforeTaskIds = filterRejectHisTask(beforeTaskIds);
        Set<String> rejectTaskIds = processRejectLog(bpmnModel, beforeTaskIds, finishedTaskIds);
        if (beforeTaskIds.size() == 1 && hasMoreThanOneAfterTask(bpmnModel, beforeTaskIds)) {
            List<Execution> executions = runtimeService.createExecutionQuery().parentId(processInstanceId).list();
            List<String> executionIds = executions.stream()
                .filter(e -> rejectTaskIds.contains(e.getActivityId()))
                .map(Execution::getId).collect(Collectors.toList());
            runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(processInstanceId)
                .moveExecutionsToSingleActivityId(executionIds, beforeTaskIds.iterator().next())
                .changeState();
            return;
        }
        runtimeService.createChangeActivityStateBuilder()
            .processInstanceId(processInstanceId)
            .moveSingleActivityIdToActivityIds(task.getTaskDefinitionKey(), beforeTaskIds)
            .changeState();
    }

    private List<String> filterRejectHisTask(List<String> beforeTaskIds) {
        List<HistoricTaskInstance> historyTasks = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(task.getProcessInstanceId())
            .taskDefinitionKeys(beforeTaskIds)
            .list();
        List<String> results = new ArrayList<>();
        String logType = FlowableTaskLogType.REJECT.getCode();
        HistoricTaskLogEntryQuery taskLogQuery = historyService.createHistoricTaskLogEntryQuery();
        for (HistoricTaskInstance historyTask : historyTasks) {
            List<HistoricTaskLogEntry> taskLogs = taskLogQuery.taskId(historyTask.getId()).type(logType).list();
            if (CollectionUtil.isEmpty(taskLogs) && !results.contains(historyTask.getTaskDefinitionKey())) {
                results.add(historyTask.getTaskDefinitionKey());
            }
        }
        return results;
    }

    private List<String> getFinishedTaskIds() {
        List<String> finishedTaskIds = new ArrayList<>();
        List<HistoricActivityInstance> allActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(task.getProcessInstanceId())
            .list();
        if (CollectionUtil.isEmpty(allActivityInstanceList)) {
            return finishedTaskIds;
        }
        for (HistoricActivityInstance item : allActivityInstanceList) {
            if (item.getEndTime() == null) {
                continue;
            }
            if (BpmnXMLConstants.ELEMENT_TASK_USER.equals(item.getActivityType())
                || BpmnXMLConstants.ELEMENT_EVENT_START.equals(item.getActivityType())) {
                finishedTaskIds.add(item.getActivityId());
            }
        }
        return finishedTaskIds;
    }

    private Set<String> processRejectLog(BpmnModel bpmnModel, List<String> beforeTaskIds, List<String> finishedTaskIds) {
        Set<String> rejectTaskIds = getRejectTaskIds(bpmnModel, beforeTaskIds, finishedTaskIds);
        String logType = FlowableTaskLogType.REJECT.getCode();
        String logData = StringUtil.format("任务[{0}]拒绝关联取消", task.getId());
        List<Task> runningTasks = taskService.createTaskQuery()
            .processInstanceId(task.getProcessInstanceId())
            .taskDefinitionKeys(rejectTaskIds)
            .list();
        for (Task runningTask : runningTasks) {
            addTaskLog(runningTask, logType, logData);
        }
        List<HistoricTaskInstance> historyTasks = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(task.getProcessInstanceId())
            .taskDefinitionKeys(rejectTaskIds)
            .list();
        HistoricTaskLogEntryQuery taskLogQuery = historyService.createHistoricTaskLogEntryQuery();
        for (HistoricTaskInstance historyTask : historyTasks) {
            List<HistoricTaskLogEntry> taskLogs = taskLogQuery.taskId(historyTask.getId()).type(logType).list();
            if (CollectionUtil.isEmpty(taskLogs)) {
                addTaskLog(historyTask, logType, logData);
            }
        }
        return rejectTaskIds;
    }

    private Set<String> getRejectTaskIds(BpmnModel bpmnModel, List<String> beforeTaskIds, List<String> finishedTaskIds) {
        List<UserTask> beforeTasks = beforeTaskIds.stream().map(taskId -> {
            return modelAssembler.getUserTaskByKey(bpmnModel, taskId);
        }).collect(Collectors.toList());
        List<String> filterTaskIds = new ArrayList<>(finishedTaskIds);
        List<Task> runningTasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        filterTaskIds.addAll(runningTasks.stream().map(Task::getTaskDefinitionKey).collect(Collectors.toList()));
        Set<String> rejectTaskIds = new HashSet<>(beforeTaskIds);
        for (UserTask beforeTask : beforeTasks) {
            rejectTaskIds.addAll(modelAssembler.getAllAfterUserTaskId(beforeTask, filterTaskIds));
        }
        return rejectTaskIds;
    }

    private boolean hasMoreThanOneAfterTask(BpmnModel bpmnModel, List<String> beforeTaskIds) {
        Set<String> afterTaskIds = new HashSet<>();
        for (String beforeTaskId : beforeTaskIds) {
            UserTask beforeTask = modelAssembler.getUserTaskByKey(bpmnModel, beforeTaskId);
            afterTaskIds.addAll(modelAssembler.getAfterUserTaskId(beforeTask));
        }
        return afterTaskIds.size() > 1;
    }

    @Override
    protected void postprocess() {
        new SendSysMessageAction(operatorCode, task.getProcessInstanceId()).run();
    }
}
