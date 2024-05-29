package com.lego.doc.dto;


import com.lego.core.dto.TreeDTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocNodeInfo extends TreeDTO<DocNodeInfo> {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String type;
    private TypeInfo page;
}
