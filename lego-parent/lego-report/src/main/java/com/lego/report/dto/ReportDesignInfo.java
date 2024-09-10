package com.lego.report.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReportDesignInfo extends DTO {

    private int sn;
    private String code;
    private String name;
    private boolean enable;
    private String position;
    private String chartType;
    private String dataDimension;
    private String creatorCode;
    private TypeInfo definition;
    private List<String> dataCategories = new ArrayList<>();

}
