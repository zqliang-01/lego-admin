package com.lego.doc.vo;


import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocBookModifyVO extends VO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String description;
    private boolean open;
    private String cover;
}
