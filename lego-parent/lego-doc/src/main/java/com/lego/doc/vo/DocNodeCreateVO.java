package com.lego.doc.vo;


import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocNodeCreateVO extends VO {

    private static final long serialVersionUID = 1L;

    private String parentCode;
    private String name;
    private String book;
    private String type;
    private String fileCode;
    private String description;
}
