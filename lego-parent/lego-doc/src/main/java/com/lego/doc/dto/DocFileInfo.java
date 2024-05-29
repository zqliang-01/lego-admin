package com.lego.doc.dto;


import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocFileInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private long size;
    private String path;
    private boolean enable;
    private TypeInfo type;
    private TypeInfo location;
}
