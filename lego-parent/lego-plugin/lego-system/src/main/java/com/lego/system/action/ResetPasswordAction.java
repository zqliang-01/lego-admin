package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.util.EntityUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysPermissionCode;

import java.text.MessageFormat;
import java.util.List;

public class ResetPasswordAction extends MaintainAction {

    private List<String> codes;

    private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

    public ResetPasswordAction(String operatorCode, List<String> codes) {
        super(SysPermissionCode.manageUser, operatorCode);
        this.codes = codes;
    }

    @Override
    protected void doRun() {
        List<SysEmployee> employees = employeeDao.findByCodes(codes);
        for (SysEmployee employee : employees) {
            employee.resetPassword(employee.getCode());
        }
        employeeDao.saveAll(employees);
        this.description = MessageFormat.format("重置员工密码[{0}]", EntityUtil.toString(employees));
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return "批量员工";
    }
}
