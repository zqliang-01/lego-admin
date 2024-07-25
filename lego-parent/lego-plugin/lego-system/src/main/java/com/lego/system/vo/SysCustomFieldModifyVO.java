package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysCustomFieldModifyVO extends VO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String relativeFormCode;
    private String generatorCode;
    private String fieldCode;
    private String name;
    private String formPosition;
    private String componentName;
    private Object defaultValue;
    private String formType;
    private String inputTips;
    private String stylePercent;
    private String optionDataType;
    private String optionDictType;
    private Integer precisions;
    private Integer maxNumRestrict;
    private Integer minNumRestrict;
    private String setting;
    private boolean hidden;
    private boolean required;
    private boolean unique;
    private Integer xAxis;
    private Integer yAxis;
    private int sn;
}
