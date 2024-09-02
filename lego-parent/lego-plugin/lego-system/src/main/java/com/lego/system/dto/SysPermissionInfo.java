package com.lego.system.dto;

import com.lego.core.dto.TreeDTO;
import com.lego.core.dto.TypeInfo;
import com.lego.system.vo.SysPermissionRouteTypeCode;
import com.lego.system.vo.SysPermissionTypeCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysPermissionInfo extends TreeDTO<SysPermissionInfo> {

    private static final long serialVersionUID = 1L;

    private String name;
    private String icon;
    private Date createTime;
    private String className;
    private int sn;
    private TypeInfo type;
    private TypeInfo routeType;
    private TypeInfo form;
    private String relateCode;

    public String getRealm() {
        String[] codes = getCode().split("_");
        return codes[codes.length - 1];
    }

    public boolean isDynamicRoute() {
        if (routeType == null) {
            return false;
        }
        return SysPermissionRouteTypeCode.DYNAMIC.equals(routeType.getCode());
    }

    public boolean isApp() {
        return type != null && SysPermissionTypeCode.APP.equals(type.getCode());
    }

    public boolean isMenu() {
        return type != null && SysPermissionTypeCode.MENU_TYPES.contains(type.getCode());
    }
}
