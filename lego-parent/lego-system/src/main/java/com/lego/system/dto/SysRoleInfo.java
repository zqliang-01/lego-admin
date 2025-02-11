package com.lego.system.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysRoleInfo extends DTO {

    private static final long serialVersionUID = 3006333688730474459L;

    private String code;
    private String name;
    private Date createTime;
    private int dataScope;
}
