package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysPermission;
import com.lego.system.entity.simpletype.SysPermissionRouteType;
import com.lego.system.entity.simpletype.SysPermissionType;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysPermissionModifyVO;
import com.lego.system.vo.SysPermissionRouteTypeCode;

public class ModifySysPermissionAction extends ModifyAction<SysPermission, ISysPermissionDao> {

    private SysPermissionModifyVO vo;

    public ModifySysPermissionAction(String operatorCode, SysPermissionModifyVO vo) {
        super(SysPermissionCode.managePermission, operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "菜单名称不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getType()), "菜单类型不能为空！");
    }

    @Override
    protected void doModify(SysPermission entity) {
        entity.setName(vo.getName());
        entity.setType(findByCode(SysPermissionType.class, vo.getType()));
        entity.setRouteType(findByUnsureCode(SysPermissionRouteType.class, vo.getRouteType()));
        entity.setIcon(vo.getIcon());
        entity.setSn(vo.getSn());
        entity.setForm(findByUnsureCode(SysCustomForm.class, vo.getForm()));
        entity.setParent(findByUnsureCode(SysPermission.class, vo.getParentCode()));
    }

    @Override
    protected void postprocess() {
        if (SysPermissionRouteTypeCode.DYNAMIC.equals(vo.getRouteType())) {
            SysPermissionRouteType dynamicRouterType = findByUnsureCode(SysPermissionRouteType.class, SysPermissionRouteTypeCode.DYNAMIC);
            updateParentRouteType(targetEntity.getParent(), dynamicRouterType);
        }
    }

    private void updateParentRouteType(SysPermission parent, SysPermissionRouteType dynamicRouterType) {
        if (parent != null) {
            parent.setRouteType(dynamicRouterType);
            entityDao.save(parent);
            updateParentRouteType(parent.getParent(), dynamicRouterType);
        }
    }
}
