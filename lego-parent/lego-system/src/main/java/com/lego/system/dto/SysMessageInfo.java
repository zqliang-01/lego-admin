package com.lego.system.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysMessageInfo extends DTO {

    private String code;
    private String name;
    private String content;
    private boolean readed;
    private Date readTime;
    private Date createTime;
    private String entityCode;
    private TypeInfo form;
    private TypeInfo type;
    private TypeInfo creator;
    private TypeInfo recipient;
}
