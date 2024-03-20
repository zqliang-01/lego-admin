package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.common.Constants;
import com.lego.core.exception.BusinessException;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysRole;
import com.lego.system.vo.SysPermissionAuthVO;
import com.lego.system.vo.SysPermissionCode;

public class AuthRoleAction extends ModifyAction<SysRole, ISysRoleDao> {

    private SysPermissionAuthVO vo;

    private ISysPermissionDao permissionDao = getDao(ISysPermissionDao.class);

    public AuthRoleAction(String operatorCode, SysPermissionAuthVO vo) {
        super(SysPermissionCode.manageRole, operatorCode, vo.getRoleCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(!Constants.ADMIN_ROLE_CODE.equals(vo.getRoleCode()), "超级管理员角色不允许进行授权变更！");
    }

    @Override
    protected void doModify(SysRole entity) {
        entity.setPermissions(permissionDao.findByCodes(vo.getPermissionCodes()));
    }
}
