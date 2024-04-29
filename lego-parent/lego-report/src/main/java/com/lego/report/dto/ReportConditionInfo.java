package com.lego.report.dto;


import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportConditionInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String sqlKey;
    private String type;
    private int sn;
    private boolean required;
    private String defaultValue;
    private String definitionCode;
    private String dependentCode;
    private String dataDefinitionCode;
}
