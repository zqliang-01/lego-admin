package com.lego.flowable.action;

import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.flowable.FlowableProcessConstants;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.handler.FlowableTaskCompleteHandler;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysGenTable;
import com.lego.system.vo.SysPermissionCode;

public class SaveFlowableTaskAction extends FlowableTaskAction {

    private FlowableTaskCompleteVO vo;

    private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);

    private FlowableTaskCompleteHandler completeHandler = LegoBeanFactory.getBean(FlowableTaskCompleteHandler.class);

    public SaveFlowableTaskAction(String operatorCode, FlowableTaskCompleteVO vo) {
        super(SysPermissionCode.oaUndo, operatorCode, vo.getId());
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
        SysCustomForm form = formDao.findByCode(task.getFormKey());
        SysGenTable genTable = form.getTable();
        BusinessException.check(genTable != null, "表单[{0}]无关联数据表，任务保存失败！", task.getFormKey());

        TaskCompletedVO completedVO = new TaskCompletedVO();
        completedVO.setSave(true);
        completedVO.setTableCode(genTable.getCode());
        completedVO.setVariable(vo.getVariables());
        completeHandler.doCompleted(genTable.getAppCode(), completedVO);

        Object code = vo.getVariables().get(FlowableProcessConstants.FORM_UNIQUE_KEY);
        taskService.setVariableLocal(vo.getId(), FlowableProcessConstants.FORM_UNIQUE_KEY, code);

        if (StringUtil.isNotBlank(vo.getComment())) {
            taskService.deleteComments(vo.getId(), null);
            taskService.addComment(vo.getId(), task.getProcessInstanceId(), vo.getComment());
        }
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }
}
