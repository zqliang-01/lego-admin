package com.lego.system.action;

import com.lego.core.action.DeleteAction;
import com.lego.core.exception.BusinessException;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysPermission;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;

public class DeleteSysPermissionAction extends DeleteAction<SysPermission, ISysPermissionDao> {

    private ISysRoleDao roleDao = getDao(ISysRoleDao.class);

    public DeleteSysPermissionAction(String operatorCode, String entityCode) {
        super(SysPermissionCode.manage, operatorCode, entityCode);
    }

    @Override
    protected void preprocess() {
        checkAuthed(targetEntity);
        List<SysPermission> childrens = entityDao.findByParent(targetEntity);
        for (SysPermission children : childrens) {
            checkAuthed(children);
            BusinessException.check(!children.isMenu(), "菜单[{0}]存在下级菜单，删除失败！", targetEntity.getName());
            new DeleteSysPermissionAction(operatorCode, children.getCode()).run();
        }
    }

    private void checkAuthed(SysPermission permission) {
        List<String> roleCodes = roleDao.findCodesBy(permission);
        BusinessException.check(roleCodes.isEmpty(), "菜单[{0}]已经授权，无法删除！", permission.getName());
    }
}
