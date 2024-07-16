package com.lego.flowable.action;

import cn.hutool.core.convert.Convert;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.flowable.FlowableProcessConstants;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.handler.FlowableTaskCompleteHandler;
import com.lego.flowable.vo.FlowableCommentType;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysGenTable;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.task.api.DelegationState;

public class CompleteFlowableTaskAction extends FlowableTaskAction {

    private FlowableTaskCompleteVO vo;

    private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);

    private FlowableTaskCompleteHandler completeHandler = LegoBeanFactory.getBean(FlowableTaskCompleteHandler.class);

    public CompleteFlowableTaskAction(String operatorCode, FlowableTaskCompleteVO vo) {
        super(SysPermissionCode.oaUndo, operatorCode, vo.getId());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        super.preprocess();
        if (StringUtil.isNotBlank(task.getFormKey())) {
            BusinessException.check(!vo.getVariables().isEmpty(), "任务完工失败，请填写表单信息！");
            Object code = taskService.getVariableLocal(vo.getId(), FlowableProcessConstants.FORM_UNIQUE_KEY);
            if (code != null) {
                Object newCode = vo.getVariables().get(FlowableProcessConstants.FORM_UNIQUE_KEY);
                BusinessException.check(code.equals(newCode), "任务已保存表单数据[{0}]与新提交数据不一致，保存失败！", code);
            }
        }
    }

    @Override
    protected void doRun() {
        processBusinessCallback();
        String commentType = FlowableCommentType.GENERIC.getCode();
        if (StringUtil.isNotBlank(vo.getComment())) {
            String comment = vo.getComment();
            if (DelegationState.PENDING.equals(task.getDelegationState())) {
                SysEmployee employee = employeeDao.findByCode(operatorCode);
                comment = employee.getName() + ":" + comment;
            }
            taskService.addComment(vo.getId(), task.getProcessInstanceId(), commentType, comment);
        }
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.resolveTask(vo.getId());
            return;
        }
        taskService.setAssignee(vo.getId(), operatorCode);
        if (vo.getVariables().isEmpty()) {
            taskService.complete(vo.getId());
            return;
        }
        Object code = vo.getVariables().get(FlowableProcessConstants.FORM_UNIQUE_KEY);
        taskService.setVariableLocal(vo.getId(), FlowableProcessConstants.FORM_UNIQUE_KEY, code);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        String localScopeValue = modelAssembler.getUserTaskAttributeValue(bpmnModel, task.getTaskDefinitionKey(), FlowableProcessConstants.FORM_LOCAL_SCOPE);
        boolean localScope = Convert.toBool(localScopeValue, false);
        taskService.complete(vo.getId(), vo.getVariables(), localScope);
    }

    private void processBusinessCallback() {
        if (StringUtil.isBlank(task.getFormKey())) {
            return;
        }
        SysCustomForm form = formDao.findByCode(task.getFormKey());
        SysGenTable genTable = form.getTable();
        BusinessException.check(genTable != null, "表单[{0}]无关联数据表，任务保存失败！", task.getFormKey());

        TaskCompletedVO completedVO = new TaskCompletedVO();
        completedVO.setTableCode(genTable.getCode());
        completedVO.setVariable(vo.getVariables());
        completeHandler.doCompleted(genTable.getAppCode(), completedVO);
    }

    @Override
    protected void postprocess() {
        new SendSysMessageAction(operatorCode, task.getProcessInstanceId()).run();
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }
}
