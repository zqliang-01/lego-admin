package com.lego.core.vo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.lego.core.common.GenConstants;
import com.lego.core.data.hibernate.BusiEntity;
import com.lego.core.util.StringUtil;

import lombok.Getter;

@Getter
public enum CustomFieldTypeEnum {

    TEXT("text", "单行文本", "FieldInput", GenConstants.COLUMNTYPE_STRING, String.class),
    TEXT_AREA("textarea", "多行文本", "FieldTextarea", GenConstants.COLUMNTYPE_TEXT, String.class),
    TEXT_JSON("jsonEditor", "JSON编辑器", "FieldTextarea", GenConstants.COLUMNTYPE_TEXT, String.class),
    WEBSITE("website", "网址", "FieldInput", GenConstants.NULL, String.class),
    BOOLEAN("boolean_value", "布尔值", "FieldBoolean", GenConstants.NULL, boolean.class),
    SELECT("select", "单选", "FieldSelect", GenConstants.NULL, String.class),
    CHECKBOX("checkbox", "多选", "FieldCheckbox", GenConstants.NULL, List.class),
    NUMBER("number", "整数", "FieldInput", GenConstants.COLUMNTYPE_NUMBER, int.class),
    FLOATNUMBER("floatnumber", "小数", "FieldInput", GenConstants.COLUMNTYPE_FLOAT, BigDecimal.class),
    PERCENT("percent", "百分数", "FieldPercent", GenConstants.NULL, BigDecimal.class),
    MOBILE("mobile", "手机", "FieldInput", GenConstants.NULL, String.class),
    EMAIL("email", "邮箱", "FieldInput", GenConstants.NULL, String.class),
    DATE("date", "日期", "FieldInput", GenConstants.COLUMNTYPE_TIME, Date.class),
    DATETIME("datetime", "日期时间", "FieldInput", GenConstants.COLUMNTYPE_TIME, Date.class),
    HANDWRITING_SIGN("handwriting_sign", "手写签名", "FieldWritingSign", GenConstants.NULL, String.class),
    DESC_TEXT("desc_text", "描述文字", "FieldDescText", GenConstants.NULL, String.class),
	EMPLOYEE("user", "员工", "FieldInput", GenConstants.NULL, String.class),
	DEPT("structure", "部门", "FieldInput", GenConstants.NULL, String.class),
	ENTITY("entity", "关联表", "FieldInput", GenConstants.COLUMNTYPE_REFERENCE_ENTITY, BusiEntity.class);

	private static final List<CustomFieldTypeEnum> ENTITY_TYPE_LIST = Arrays.asList(SELECT, CHECKBOX, ENTITY);

	private static final List<CustomFieldTypeEnum> TYPE_INFO_LIST = Arrays.asList(SELECT, CHECKBOX, EMPLOYEE, DEPT, ENTITY);

	/**
	 * 通用类型，公共模块的字段，涉及跨服务查询
	 */
	private static final List<CustomFieldTypeEnum> COMMON_TYPE_LIST = Arrays.asList(EMPLOYEE, DEPT);

	private String code;
	private String name;
	private String componentName;
	private List<String> dataType;
	private Class<?> type;

	private CustomFieldTypeEnum(String code, String name, String componentName, List<String> dataType, Class<?> type) {
		this.code = code;
		this.name = name;
		this.componentName = componentName;
		this.dataType = dataType;
		this.type = type;
	}

	public boolean equals(String code) {
		return this.getCode().equals(code);
	}

	public boolean equals(CustomFieldTypeEnum fieldType) {
		return this.equals(fieldType.getCode());
	}

	public static CustomFieldTypeEnum get(String code) {
		for (CustomFieldTypeEnum value : values()) {
			if (value.equals(code)) {
				return value;
			}
		}
		return null;
	}

	public boolean isEntity() {
		return ENTITY_TYPE_LIST.contains(this);
	}

	public boolean isTypeInfo() {
		return TYPE_INFO_LIST.contains(this);
	}

	public boolean isCommon() {
		return COMMON_TYPE_LIST.contains(this);
	}

	public static Class<?> getType(String code) {
		if (StringUtil.isBlank(code)) {
			return String.class;
		}
		CustomFieldTypeEnum fieldType = get(code);
		if (fieldType != null) {
			return fieldType.getType();
		}
		return String.class;
	}

	public static CustomFieldTypeEnum getByDataType(String dataType) {
		for (CustomFieldTypeEnum value : values()) {
			if (value.getDataType().contains(dataType)) {
				return value;
			}
		}
		return TEXT;
	}
}
