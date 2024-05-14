package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysPermission;
import com.lego.system.entity.simpletype.SysPermissionRouteType;
import com.lego.system.entity.simpletype.SysPermissionType;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysPermissionModifyVO;
import com.lego.system.vo.SysPermissionRouteTypeCode;

import java.util.List;

public class ModifySysPermissionAction extends ModifyAction<SysPermission, ISysPermissionDao> {

    private SysPermissionModifyVO vo;

    private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);

    public ModifySysPermissionAction(String operatorCode, SysPermissionModifyVO vo) {
        super(SysPermissionCode.managePermission, operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "菜单名称不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getType()), "菜单类型不能为空！");

        SysCustomForm form = formDao.findByUnsureCode(vo.getForm());
        if (form != null) {
            List<SysPermission> permissions = entityDao.findBy(form);
            permissions.stream().forEach(p -> {
                if (!p.equals(targetEntity)) {
                    p.setForm(null);
                }
            });
            entityDao.saveAll(permissions);
        }
    }

    @Override
    protected void doModify(SysPermission entity) {
        entity.setName(vo.getName());
        entity.setType(findByCode(SysPermissionType.class, vo.getType()));
        if (StringUtil.isNotBlank(vo.getRouteType())) {
            entity.setRouteType(findByUnsureCode(SysPermissionRouteType.class, vo.getRouteType()));
        }
        entity.setIcon(vo.getIcon());
        entity.setSn(vo.getSn());
        entity.setForm(formDao.findByUnsureCode(vo.getForm()));
        entity.setParent(entityDao.findByUnsureCode(vo.getParentCode()));
        entity.setReportCode(vo.getReportCode());
    }

    @Override
    protected void postprocess() {
        if (SysPermissionRouteTypeCode.DYNAMIC.equals(vo.getRouteType())) {
            SysPermissionRouteType dynamicRouterType = findByUnsureCode(SysPermissionRouteType.class, SysPermissionRouteTypeCode.DYNAMIC);
            updateParentRouteType(targetEntity.getParent(), dynamicRouterType);
        }

        List<SysCustomForm> forms = formDao.findBy(targetEntity);
        forms.stream().forEach(form -> form.setPermission(null));
        formDao.saveAll(forms);

        SysCustomForm form = formDao.findByUnsureCode(vo.getForm());
        if (form != null && !EntityUtil.getCode(form.getPermission()).equals(vo.getForm())) {
            form.setPermission(targetEntity);
            formDao.save(form);
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
