package com.lego.report.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDesignSimpleInfo extends DTO {

    private int sn;
    private String code;
    private String name;
    private boolean enable;
    private String position;
    private String chartType;
    private String definitionCode;
}
