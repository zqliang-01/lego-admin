package com.lego.system.dto;

import com.lego.core.common.GenConstants;
import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysGenTableColumnInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private int sn;
    private String code;
    private String name;
    private String comment;
    private String dataType;
    private String formType;
    private String javaField;
    private String javaFieldType;
    private boolean required;
    private boolean unique;
    private TypeInfo table;
    private SysGenTableInfo relativeTable;
    private TypeInfo creator;

    public boolean isTypeInfo() {
        CustomFieldTypeEnum fieldType = CustomFieldTypeEnum.get(formType);
        if (fieldType == null) {
            return false;
        }
        return fieldType.isTypeInfo();
    }

    public boolean isEntityType() {
        CustomFieldTypeEnum fieldType = CustomFieldTypeEnum.get(formType);
        if (fieldType == null) {
            return false;
        }
        return fieldType.isEntity();
    }

    public boolean isCommonType() {
        CustomFieldTypeEnum fieldType = CustomFieldTypeEnum.get(formType);
        if (fieldType == null) {
            return false;
        }
        return fieldType.isCommon();
    }

    public boolean isIgnoreField() {
    	return GenConstants.COLUMNNAME_IGNORE_ENTITY.contains(name);
    }

    public String getClassName() {
        if (StringUtil.isBlank(javaFieldType)) {
            return javaFieldType;
        }
        int pos = javaFieldType.lastIndexOf(".");
        if (pos == -1) {
            return "";
        }
        return javaFieldType.substring(pos + 1);
    }

    public String getFirstUpperField() {
        return StringUtil.toFirstUpper(javaField);
    }
}
