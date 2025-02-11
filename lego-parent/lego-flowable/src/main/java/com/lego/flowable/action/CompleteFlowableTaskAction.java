package com.lego.flowable.action;

import cn.hutool.core.convert.Convert;
import com.lego.core.data.ActionType;
import com.lego.core.dto.FormInfo;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.module.flowable.FlowableProcessConstants;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.handler.IFlowableCompleteHandler;
import com.lego.flowable.vo.FlowableCommentType;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.task.api.DelegationState;

public class CompleteFlowableTaskAction extends FlowableTaskAction {

    private FlowableTaskCompleteVO vo;

    private IFlowableCompleteHandler completeHandler = LegoBeanFactory.getBean(IFlowableCompleteHandler.class);

    public CompleteFlowableTaskAction(String operatorCode, FlowableTaskCompleteVO vo) {
        super("oa_undo", operatorCode, vo.getId());
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
        String commentType = FlowableCommentType.GENERIC.getCode();
        String comment = vo.getComment();
        BusinessException.check(StringUtil.isNotBlank(comment), "审批意见不能为空！");
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            TypeInfo employee = commonService.findEmployeeBy(operatorCode);
            comment = employee.getName() + ":" + comment;
        }
        taskService.addComment(vo.getId(), task.getProcessInstanceId(), commentType, comment);
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.resolveTask(vo.getId());
            return;
        }
        taskService.setAssignee(vo.getId(), operatorCode);
        if (StringUtil.isBlank(task.getFormKey())) {
            taskService.complete(vo.getId());
            return;
        }
        String code = processBusinessCallback();
        taskService.setVariableLocal(vo.getId(), FlowableProcessConstants.FORM_UNIQUE_KEY, code);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        String localScopeValue = modelAssembler.getUserTaskAttributeValue(bpmnModel, task.getTaskDefinitionKey(), FlowableProcessConstants.FORM_LOCAL_SCOPE);
        boolean localScope = Convert.toBool(localScopeValue, false);
        taskService.complete(vo.getId(), vo.getVariables(), localScope);
    }

    private String processBusinessCallback() {
        if (StringUtil.isBlank(task.getFormKey())) {
            return "";
        }
        FormInfo form = commonService.findFormBy(task.getFormKey());
        BusinessException.check(form != null, "表单[{0}]无关联数据表，任务完工失败！", task.getFormKey());

        TaskCompletedVO completedVO = new TaskCompletedVO();
        completedVO.setTableCode(form.getTableCode());
        completedVO.setVariable(vo.getVariables());
        return completeHandler.doTaskCompleted(form.getAppCode(), completedVO);
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
