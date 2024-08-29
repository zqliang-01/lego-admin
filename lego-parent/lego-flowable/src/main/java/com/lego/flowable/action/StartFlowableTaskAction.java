package com.lego.flowable.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.handler.IFlowableCompleteHandler;
import com.lego.flowable.vo.FlowableTaskStartVO;
import com.lego.flowable.vo.ProcessStatus;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysGenTable;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;

import java.text.MessageFormat;

public class StartFlowableTaskAction extends MaintainAction {

    private FlowableTaskStartVO vo;
    private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);
    private RuntimeService runtimeService = LegoBeanFactory.getBean(RuntimeService.class);
    private IdentityService identityService = LegoBeanFactory.getBean(IdentityService.class);
    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);
    private IFlowableCompleteHandler completeHandler = LegoBeanFactory.getBean(IFlowableCompleteHandler.class);

    public StartFlowableTaskAction(String operatorCode, FlowableTaskStartVO vo) {
        super(SysPermissionCode.manageWorkFlow, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void doRun() {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(vo.getDefinitionId())
            .latestVersion()
            .active();
        ProcessDefinition definition = processDefinitionQuery.singleResult();
        BusinessException.check(definition != null, "模型[{0}]不存在正常可用的流程定义，启动流程失败！", vo.getDefinitionId());

        Deployment deployment = repositoryService.createDeploymentQuery()
            .deploymentId(definition.getDeploymentId())
            .singleResult();

        String code = processBusinessCallback();
        identityService.setAuthenticatedUserId(operatorCode);
        vo.getVariables().put(BpmnXMLConstants.ATTRIBUTE_EVENT_START_INITIATOR, operatorCode);

        // 构建发起流程实例
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        processInstanceBuilder.name(deployment.getName());
        processInstanceBuilder.processDefinitionId(definition.getId());
        processInstanceBuilder.variables(vo.getVariables());
        processInstanceBuilder.businessKey(code);
        processInstanceBuilder.businessStatus(ProcessStatus.RUNNING.getCode());
        ProcessInstance instance = processInstanceBuilder.start();
        this.description = "发起流程" + instance.getId();

        processSysMessage(instance.getId());
    }

    private String processBusinessCallback() {
        if (StringUtil.isBlank(vo.getFormKey())) {
            return "";
        }
        SysCustomForm form = formDao.findByCode(vo.getFormKey());
        SysGenTable genTable = form.getTable();
        BusinessException.check(genTable != null, "表单[{0}]无关联数据表，任务保存失败！", vo.getFormKey());

        TaskCompletedVO completedVO = new TaskCompletedVO();
        completedVO.setVariable(vo.getVariables());
        completedVO.setTableCode(genTable.getCode());
        return completeHandler.doTaskCompleted(genTable.getAppCode(), completedVO);
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
        return MessageFormat.format("流程定义[{0}]", vo.getDefinitionId());
    }
}
