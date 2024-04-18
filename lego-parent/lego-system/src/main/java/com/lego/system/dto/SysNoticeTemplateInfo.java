package com.lego.system.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SysNoticeTemplateInfo extends DTO {

    private String code;
    private String name;
    private String content;
    private boolean published;
    private Date publishedTime;
    private TypeInfo creator;
    private List<TypeInfo> employees;
    private List<TypeInfo> depts;
}
