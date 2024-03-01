package com.lego.system.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysOperationLogInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String action;
    private String entityCode;
    private String description;
    private Date createTime;
    private TypeInfo permission;
    private TypeInfo operator;
}
