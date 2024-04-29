package com.lego.report.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportConditionVO extends VO {

    private String sqlKey;
    private String name;
    private String type;
    private int sn;
    private boolean required;
    private String defaultValue;
    private String definition;
    private String dependentCode;
    private String dataDefinitionCode;
}
