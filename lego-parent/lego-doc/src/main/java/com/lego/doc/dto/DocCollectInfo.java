package com.lego.doc.dto;


import com.lego.core.dto.BusDTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DocCollectInfo extends BusDTO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private boolean enable;
    private TypeInfo node;
    private String type;
    private TypeInfo book;
    private Date createTime;
}
