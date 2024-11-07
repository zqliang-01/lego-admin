package com.lego.flowable.action;

import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.assembler.FlowableModelAssembler;
import com.lego.flowable.vo.FlowableTaskLogType;
import com.lego.flowable.vo.ProcessStatus;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class StopFlowableInstanceAction extends FlowableAction {

    private String id;
    private ProcessInstance processInstance;

    private RuntimeService runtimeService = LegoBeanFactory.getBean(RuntimeService.class);

    private FlowableModelAssembler modelAssembler = LegoBeanFactory.getBean(FlowableModelAssembler.class);

    public StopFlowableInstanceAction(String operatorCode, String id) {
        super(SysPermissionCode.manageWorkFlow, operatorCode);
        this.id = id;
    }

    @Override
    protected void preprocess() {
        processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(id)
            .singleResult();
        BusinessException.check(processInstance != null, "不存在有效的流程实例[{0}]，实例终止失败！", id);
        ProcessStatus status = ProcessStatus.get(processInstance.getBusinessStatus());
        BusinessException.check(status.isRunning(), "流程[{0}]非运行中状态，流程终止失败！", id);
    }

    @Override
    protected void doRun() {
        runtimeService.updateBusinessStatus(id, ProcessStatus.TERMINATED.getCode());

        String logType = FlowableTaskLogType.REJECT.getCode();
        List<Task> runningTasks = taskService.createTaskQuery()
            .processInstanceId(id)
            .list();
        for (Task runningTask : runningTasks) {
            addTaskLog(runningTask, logType, "流程终止关联取消");
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        EndEvent endEvent = modelAssembler.getEndEvent(bpmnModel);
        List<Execution> executions = runtimeService.createExecutionQuery().parentId(id).list();
        List<String> executionIds = executions.stream().map(Execution::getId).collect(Collectors.toList());
        executions.forEach(execution -> executionIds.add(execution.getId()));
        runtimeService.createChangeActivityStateBuilder()
            .processInstanceId(id)
            .moveExecutionsToSingleActivityId(executionIds, endEvent.getId())
            .changeState();
        this.description = MessageFormat.format("终止流程[{0}]", processInstance.getId());
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return MessageFormat.format("流程[{0}]", processInstance.getName());
    }
}
