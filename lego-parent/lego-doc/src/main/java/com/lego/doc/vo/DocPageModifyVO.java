package com.lego.doc.vo;


import com.lego.core.vo.ModifyVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocPageModifyVO extends ModifyVO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String content;
}
