package com.lego.flowable.action;

import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.flowable.vo.FlowableCommentType;
import com.lego.flowable.vo.FlowableTaskDelegateVO;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysPermissionCode;

public class DelegateFlowableTaskAction extends FlowableTaskAction {

    private FlowableTaskDelegateVO vo;

    public DelegateFlowableTaskAction(String operatorCode, FlowableTaskDelegateVO vo) {
        super(SysPermissionCode.oaUndo, operatorCode, vo.getId());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        super.preprocess();
        BusinessException.check(StringUtil.isNotBlank(vo.getEmployeeCode()), "委派工号不能为空！");
        BusinessException.check(!StringUtil.equals(task.getAssignee(), vo.getEmployeeCode()), "当前任务已经是您负责，无法委派给自己！");
    }

    @Override
    protected void doRun() {
        SysEmployee employee = employeeDao.findByCode(operatorCode);
        StringBuilder commentBuilder = new StringBuilder(employee.getName()).append("->");
        SysEmployee targetEmployee = employeeDao.findByCode(vo.getEmployeeCode());
        commentBuilder.append(EntityUtil.getName(targetEmployee));
        if (StringUtil.isNotBlank(vo.getComment())) {
            commentBuilder.append(": ").append(vo.getComment());
        }
        // 添加审批意见
        taskService.addComment(vo.getId(), task.getProcessInstanceId(), FlowableCommentType.DELEGATE.getCode(), commentBuilder.toString());
        // 设置办理人为当前登录人
        taskService.setOwner(vo.getId(), operatorCode);
        // 执行委派
        taskService.delegateTask(vo.getId(), vo.getEmployeeCode());
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }
}
