package com.lego.doc.dto;


import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DocNodeDetailInfo extends DocNodeInfo {

    private static final long serialVersionUID = 1L;

    private boolean editable;
    private TypeInfo creator;
    private Date createTime;
    private DocFileInfo file;
}
