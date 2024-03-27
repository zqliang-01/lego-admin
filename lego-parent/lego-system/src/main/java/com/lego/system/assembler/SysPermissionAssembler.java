package com.lego.system.assembler;

import com.alibaba.fastjson.JSONObject;
import com.lego.core.assembler.TreeAssembler;
import com.lego.core.util.EntityUtil;
import com.lego.system.dto.SysAppInfo;
import com.lego.system.dto.SysPermissionInfo;
import com.lego.system.entity.SysPermission;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SysPermissionAssembler extends TreeAssembler<SysPermissionInfo, SysPermission> {

    @Override
    protected SysPermissionInfo doCreate(SysPermission entity) {
        SysPermissionInfo info = new SysPermissionInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setIcon(entity.getIcon());
        info.setSn(entity.getSn());
        info.setType(createTypeInfo(entity.getType()));
        info.setRouteType(createTypeInfo(entity.getRouteType()));
        info.setForm(createTypeInfo(entity.getForm()));
        info.setCreateTime(entity.getCreateTime());
        info.setParentCode(EntityUtil.getCode(entity.getParent()));
        return info;
    }

    public List<SysAppInfo> createApp(List<SysPermission> permissions) {
        List<SysAppInfo> infos = new ArrayList<SysAppInfo>();
        for (SysPermission permission : permissions) {
            SysAppInfo info = new SysAppInfo();
            info.setCode(permission.getCode());
            info.setPath("/" + permission.getCode());
            info.setName(permission.getName());
            info.setIcon(permission.getIcon());
            infos.add(info);
        }
        return infos;
    }

    public JSONObject createAuth(List<SysPermissionInfo> permissions, List<String> validApps) {
        JSONObject auth = new JSONObject();
        for (SysPermissionInfo permission : permissions) {
            if (permission.isApp() && !validApps.contains(permission.getCode())) {
                continue;
            }
            List<SysPermissionInfo> childrens = permission.getChildrens();
            if (!childrens.isEmpty()) {
                JSONObject value = createAuth(childrens, validApps);
                value.put("sn", permission.getSn());
                value.put("code", permission.getCode());
                value.put("title", permission.getName());
                value.put("icon", permission.getIcon());
                value.put("formCode", permission.getForm().getCode());
                auth.put(permission.getRealm(), value);
                continue;
            }
            auth.put(permission.getRealm(), true);
        }
        return auth;
    }

    private boolean hasMenuChildren(List<SysPermissionInfo> childrens) {
        for (SysPermissionInfo children : childrens) {
            if (children.isMenu()) {
                return true;
            }
        }
        return false;
    }
}
