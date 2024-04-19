package com.lego.flowable.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.vo.ProcessStatus;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;

import java.text.MessageFormat;
import java.util.Map;

public class StartFlowableTaskAction extends MaintainAction {

    private String definitionId;
    private Map<String, Object> variables;

    private TaskService taskService = LegoBeanFactory.getBean(TaskService.class);
    private RuntimeService runtimeService = LegoBeanFactory.getBean(RuntimeService.class);
    private IdentityService identityService = LegoBeanFactory.getBean(IdentityService.class);
    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);


    public StartFlowableTaskAction(String operatorCode, String definitionId, Map<String, Object> variables) {
        super(SysPermissionCode.manageWorkFlow, operatorCode);
        this.definitionId = definitionId;
        this.variables = variables;
    }

    @Override
    protected void doRun() {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(definitionId)
            .latestVersion()
            .active();
        ProcessDefinition definition = processDefinitionQuery.singleResult();
        BusinessException.check(definition != null, "模型[{0}]不存在正常可用的流程定义，启动流程失败！", definitionId);

        Deployment deployment = repositoryService.createDeploymentQuery()
            .deploymentId(definition.getDeploymentId())
            .singleResult();

        identityService.setAuthenticatedUserId(operatorCode);
        variables.put(BpmnXMLConstants.ATTRIBUTE_EVENT_START_INITIATOR, operatorCode);

        // 构建发起流程实例
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        processInstanceBuilder.name(deployment.getName());
        processInstanceBuilder.processDefinitionId(definition.getId());
        processInstanceBuilder.variables(variables);
        processInstanceBuilder.businessStatus(ProcessStatus.RUNNING.getCode());
        ProcessInstance instance = processInstanceBuilder.start();
        this.description = "发起流程" + instance.getId();

        processSysMessage(instance.getId());
    }

    private void processSysMessage(String id) {
        new SendSysMessageAction(operatorCode, id).run();
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ADD;
    }

    @Override
    protected String getEntityName() {
        return MessageFormat.format("流程定义[{0}]", definitionId);
    }
}
