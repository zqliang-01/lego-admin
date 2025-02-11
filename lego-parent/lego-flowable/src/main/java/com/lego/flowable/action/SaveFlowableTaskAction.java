package com.lego.flowable.action;

import com.lego.core.data.ActionType;
import com.lego.core.dto.FormInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.module.flowable.FlowableProcessConstants;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.handler.IFlowableCompleteHandler;
import com.lego.flowable.vo.FlowableTaskCompleteVO;

public class SaveFlowableTaskAction extends FlowableTaskAction {

    private FlowableTaskCompleteVO vo;

    private IFlowableCompleteHandler completeHandler = LegoBeanFactory.getBean(IFlowableCompleteHandler.class);

    public SaveFlowableTaskAction(String operatorCode, FlowableTaskCompleteVO vo) {
        super("oa_undo", operatorCode, vo.getId());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        super.preprocess();
        if (StringUtil.isNotBlank(task.getFormKey())) {
            BusinessException.check(!vo.getVariables().isEmpty(), "任务保存失败，请填写表单信息！");
            Object code = taskService.getVariableLocal(vo.getId(), FlowableProcessConstants.FORM_UNIQUE_KEY);
            if (code != null) {
                Object newCode = vo.getVariables().get(FlowableProcessConstants.FORM_UNIQUE_KEY);
                BusinessException.check(code.equals(newCode), "任务已保存表单数据[{0}]与新提交数据不一致，保存失败！", code);
            }
        }
    }

    @Override
    protected void doRun() {
        if (StringUtil.isBlank(task.getFormKey())) {
            return;
        }
        FormInfo form = commonService.findFormBy(task.getFormKey());
        BusinessException.check(form != null, "表单[{0}]无关联数据表，任务保存失败！", task.getFormKey());

        TaskCompletedVO completedVO = new TaskCompletedVO();
        completedVO.setTableCode(form.getTableCode());
        completedVO.setVariable(vo.getVariables());
        String code = completeHandler.doTaskCompleted(form.getAppCode(), completedVO);
        taskService.setVariableLocal(vo.getId(), FlowableProcessConstants.FORM_UNIQUE_KEY, code);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }
}
