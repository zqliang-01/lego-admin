package com.lego.system.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysCustomFieldInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String fieldCode;
    private String name;
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
    private Object setting;
    private boolean hidden;
    private boolean required;
    private boolean unique;
    private Integer xAxis;
    private Integer yAxis;
    private Integer width;
    private String sortCode;
    private TypeInfo relativeForm;
    private TypeInfo codeGenerator;

    @Override
    public int hashCode() {
        if (StringUtil.isNotBlank(code)) {
            return code.hashCode();
        }
        return super.hashCode();
    }

    public boolean isTips() {
        return CustomFieldTypeEnum.DESC_TEXT.equals(formType);
    }
}
