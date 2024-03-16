package com.lego.system.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysCustomFormInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String appCode;
    private boolean enable;
    private String queryApiUrl;
    private String detailApiUrl;
    private String simpleApiUrl;
    private String addApiUrl;
    private String updateApiUrl;
    private String deleteApiUrl;
    private String exportApiUrl;
    private String exportAllApiUrl;
    private TypeInfo table;
    private Date createTime;
}
