package com.lego.system.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysPrintLogCreateVO {

    private String content;
    private String entityCode;
    private String templateCode;
    private String permissionCode;
}
