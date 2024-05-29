package com.lego.doc.vo;


import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocBookCreateVO extends VO {

    private static final long serialVersionUID = 1L;

    private String name;
    private boolean open;
    private String cover;
    private String description;
}
