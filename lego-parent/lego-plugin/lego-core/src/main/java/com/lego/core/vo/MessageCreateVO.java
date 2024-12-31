package com.lego.core.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageCreateVO extends VO {

    private String name;
    private String type;
    private String content;
    private String creator;
    private String recipient;
    private String entityCode;
    private String formCode;
}
