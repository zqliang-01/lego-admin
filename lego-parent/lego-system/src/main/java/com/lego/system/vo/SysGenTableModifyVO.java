package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysGenTableModifyVO extends VO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String path;
    private String comment;
    private String appCode;
    private String urlName;
    private String fieldName;
    private String className;
    private String packageName;
    private String permissionCode;
    private String dataSource;
}
