package com.lego.report.dto;


import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportTitleInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String sqlKey;
    private int sn;
    private String align;
    private TypeInfo definition;
}
