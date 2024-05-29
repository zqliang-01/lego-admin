package com.lego.doc.vo;


import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocFileCreateVO extends VO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private int size;
    private String path;
    private boolean enable;
    private String type;
    private String location;
}
