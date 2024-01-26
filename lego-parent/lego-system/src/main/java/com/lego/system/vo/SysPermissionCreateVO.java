package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysPermissionCreateVO extends VO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String icon;
    private String type;
    private String routeType;
    private String parentCode;
    private int sn;
    private String form;
}
