package com.lego.system.vo;


import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysPrintTemplateCreateVO extends VO {

    private static final long serialVersionUID = 1L;

    private String name;
    private String content;
    private String form;
}
