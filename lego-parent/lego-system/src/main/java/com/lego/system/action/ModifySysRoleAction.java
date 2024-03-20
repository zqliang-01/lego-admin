package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.common.Constants;
import com.lego.core.exception.BusinessException;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysRole;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysRoleModifyVO;

public class ModifySysRoleAction extends ModifyAction<SysRole, ISysRoleDao> {

    private String name;

    public ModifySysRoleAction(String operatorCode, SysRoleModifyVO vo) {
        super(SysPermissionCode.manageRole, operatorCode, vo.getCode());
        this.name = vo.getName();
    }

    @Override
    protected void preprocess() {
        BusinessException.check(!Constants.ADMIN_ROLE_CODE.equals(targetEntity.getCode()), "超级管理员角色不允许进行信息修改！");
    }

    @Override
    protected void doModify(SysRole entity) {
        entity.setName(name);
    }
}
