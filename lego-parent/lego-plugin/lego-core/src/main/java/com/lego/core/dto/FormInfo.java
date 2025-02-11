package com.lego.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FormInfo extends DTO {

    private String code;
    private String name;
    private String appCode;
    private String tableCode;
}
