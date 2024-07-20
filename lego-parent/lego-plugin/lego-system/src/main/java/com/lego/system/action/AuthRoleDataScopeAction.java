package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.common.Constants;
import com.lego.core.exception.BusinessException;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysRole;
import com.lego.system.vo.SysDataScopeAuthVO;
import com.lego.system.vo.SysPermissionCode;

public class AuthRoleDataScopeAction extends ModifyAction<SysRole, ISysRoleDao> {

    private SysDataScopeAuthVO vo;

    private ISysPermissionDao permissionDao = getDao(ISysPermissionDao.class);

    public AuthRoleDataScopeAction(String operatorCode, SysDataScopeAuthVO vo) {
        super(SysPermissionCode.manageRole, operatorCode, vo.getRoleCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(!Constants.ADMIN_ROLE_CODE.equals(vo.getRoleCode()), "超级管理员角色不允许进行数据权限变更！");
    }

    @Override
    protected void doModify(SysRole entity) {
        entity.setDataScope(vo.getDataScope());
    }
}
