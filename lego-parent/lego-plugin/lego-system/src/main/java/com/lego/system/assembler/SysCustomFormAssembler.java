package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysCustomFormInfo;
import com.lego.system.dto.SysCustomFormPermissionInfo;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysPermission;
import org.springframework.stereotype.Component;

@Component
public class SysCustomFormAssembler extends EntityAssembler<SysCustomFormInfo, SysCustomForm> {

    @Override
    protected SysCustomFormInfo doCreate(SysCustomForm entity) {
        SysCustomFormInfo info = new SysCustomFormInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setAppCode(entity.getTable().getAppCode());
        info.setPermissionCode(entity.getTable().getPermissionCode());
        info.setEnable(entity.isEnable());
        info.setQueryApiUrl(entity.getQueryApiUrl());
        info.setDeleteApiUrl(entity.getDeleteApiUrl());
        info.setUpdateApiUrl(entity.getUpdateApiUrl());
        info.setDetailApiUrl(entity.getDetailApiUrl());
        info.setSimpleApiUrl(entity.getSimpleApiUrl());
        info.setAddApiUrl(entity.getAddApiUrl());
        info.setExportAllApiUrl(entity.getExportAllApiUrl());
        info.setExportApiUrl(entity.getExportApiUrl());
        info.setTable(createTypeInfo(entity.getTable()));
        info.setCreateTime(entity.getCreateTime());
        return info;
    }

    public SysCustomFormPermissionInfo createPermission(SysCustomForm form, SysPermission permission) {
        SysCustomFormPermissionInfo info = new SysCustomFormPermissionInfo();
        info.setCode(form.getCode());
        info.setIcon(permission.getIcon());
        info.setClassName(form.getTable().getClassName());
        info.setPermissionCode(permission.getCode());
        info.setPermissionName(permission.getName());
        return info;
    }
}
