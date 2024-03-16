package com.lego.flowable.action;

import cn.hutool.core.convert.Convert;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.flowable.FlowableProcessConstants;
import com.lego.core.flowable.IFlowableTaskCompletedListener;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.system.service.ISysCustomFormService;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.task.api.DelegationState;

import java.util.List;

public class CompleteFlowableTaskAction extends FlowableTaskAction {

    private FlowableTaskCompleteVO vo;

    private ISysCustomFormService formService = LegoBeanFactory.getBean(ISysCustomFormService.class);

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
        if (StringUtil.isNotBlank(vo.getComment())) {
            taskService.deleteComments(vo.getId(), null);
            taskService.addComment(vo.getId(), task.getProcessInstanceId(), vo.getComment());
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
        String tableCode = formService.findTableCodeBy(task.getFormKey());
        if (StringUtil.isBlank(tableCode)) {
            return;
        }
        List<IFlowableTaskCompletedListener> listeners = LegoBeanFactory.getBeans(IFlowableTaskCompletedListener.class);
        for (IFlowableTaskCompletedListener listener : listeners) {
            if (tableCode.equals(listener.getTableCode())) {
                listener.completed(false, vo.getVariables());
            }
        }
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }
}
