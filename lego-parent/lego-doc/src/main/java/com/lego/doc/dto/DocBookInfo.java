package com.lego.doc.dto;


import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DocBookInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private boolean open;
    private boolean enable;
    private String cover;
    private String description;
    private Date createTime;
    private Date updateTime;
    private TypeInfo creator;
    private boolean editable;
}
