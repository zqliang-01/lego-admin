package com.lego.system.vo;


import com.lego.core.vo.ModifyVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysPrintTemplateModifyVO extends ModifyVO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String form;
    private String content;
}
