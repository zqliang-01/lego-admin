package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysPermission;
import com.lego.system.entity.simpletype.SysPermissionRouteType;
import com.lego.system.entity.simpletype.SysPermissionType;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysPermissionCreateVO;
import com.lego.system.vo.SysPermissionRouteTypeCode;
import com.lego.system.vo.SysPermissionTypeCode;

import java.util.Map;
import java.util.TreeMap;

public class AddSysPermissionAction extends AddAction<SysPermission, ISysPermissionDao> {

    private static final Map<String, String> authList = new TreeMap<String, String>() {
        {
            put("read", "查看列表");
            put("detail", "查看明细");
            put("add", "新增");
            put("delete", "删除");
            put("update", "更新");
            put("export", "导出");
        }
    };
    private SysPermissionCreateVO vo;

    public AddSysPermissionAction(String operatorCode, SysPermissionCreateVO vo) {
        super(SysPermissionCode.managePermission, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "菜单编码不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "菜单名称不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getType()), "菜单类型不能为空！");

        SysPermission permission = entityDao.findByUnsureCode(vo.getCode());
        BusinessException.check(permission == null, "已存在编码为[{0}]的菜单信息！", vo.getCode());

        if (SysPermissionTypeCode.AUTH.equals(vo.getType())) {
            return;
        }

        if (SysPermissionTypeCode.MENU.equals(vo.getType())) {
            String code = "";
            String[] codes = vo.getCode().split("_");
            for (int i = 0; i < codes.length - 1; i++) {
                code += codes[i];
                permission = entityDao.findByUnsureCode(code);
                BusinessException.check(permission != null, "菜单编码第[{0}]位不符合规则，编码格式应为：一级菜单_二级菜单...", i + 1);
                code += "_";
            }
        }
    }

    @Override
    protected SysPermission createTargetEntity() {
        SysPermission permission = new SysPermission(vo.getCode(), vo.getName());
        permission.setType(findByCode(SysPermissionType.class, vo.getType()));
        permission.setRouteType(findByUnsureCode(SysPermissionRouteType.class, vo.getRouteType()));
        permission.setParent(entityDao.findByUnsureCode(vo.getParentCode()));
        permission.setForm(findByUnsureCode(SysCustomForm.class, vo.getForm()));
        permission.setIcon(vo.getIcon());
        permission.setSn(vo.getSn());
        return permission;
    }

    @Override
    protected void postprocess() {
        if (SysPermissionRouteTypeCode.DYNAMIC.equals(vo.getRouteType())) {
            SysPermissionRouteType dynamicRouterType = findByUnsureCode(SysPermissionRouteType.class, SysPermissionRouteTypeCode.DYNAMIC);
            updateParentRouteType(targetEntity.getParent(), dynamicRouterType);
        }
        if (this.targetEntity.isMenu() && vo.isCreateAuth()) {
            int sn = targetEntity.getSn() * 10;
            for (Map.Entry<String, String> entry : authList.entrySet()) {
                SysPermissionCreateVO addVO = new SysPermissionCreateVO();
                addVO.setCode(targetEntity.getCode() + "_" + entry.getKey());
                addVO.setName(entry.getValue());
                addVO.setType(SysPermissionTypeCode.AUTH);
                addVO.setParentCode(targetEntity.getCode());
                addVO.setSn(++sn);
                new AddSysPermissionAction(operatorCode, addVO).run();
            }
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
