package com.lego.system.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.enums.FieldTypeEnum;
import com.lego.core.module.gen.GenConstants;
import com.lego.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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
    private TypeInfo relativeTable;
    private TypeInfo creator;
    private Map<String, String> attributes;

    // 代码生成使用
    public boolean isIgnoreField() {
    	return GenConstants.COLUMNNAME_IGNORE_ENTITY.contains(name);
    }

    // 代码生成使用
    public boolean isEntityType() {
        return FieldTypeEnum.ENTITY.equals(formType);
    }

    // 代码生成使用
    public boolean isTypeInfo() {
        FieldTypeEnum fieldType = FieldTypeEnum.get(formType);
        if (fieldType == null) {
            return false;
        }
        return fieldType.isPublic() || fieldType == FieldTypeEnum.ENTITY;
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

    // 代码生成使用
    public String getFirstUpperField() {
        return StringUtil.toFirstUpper(javaField);
    }
}
