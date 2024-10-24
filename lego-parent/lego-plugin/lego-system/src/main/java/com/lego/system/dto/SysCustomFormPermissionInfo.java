package com.lego.system.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysCustomFormPermissionInfo extends DTO {

    private String code;
    private String icon;
    private String permissionName;
    private String permissionCode;
    private String className;
}
