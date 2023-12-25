package com.lego.core.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lego.core.util.DateUtil;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericSearchItemVO extends VO {

	private static final long serialVersionUID = 1L;

	private String type;
	private String fieldCode;
	private String formType;
	private List<String> values = new ArrayList<String>();

	public List<Object> getValue(CustomFieldTypeEnum fieldType) {
		List<Object> results = new ArrayList<Object>();
		for (String value : values) {
			results.add(getTypeValue(fieldType, value));
		}
		return results;
	}

	public Object getFirstValue(CustomFieldTypeEnum fieldType) {
		if (values.isEmpty()) {
			return null;
		}
		String value = values.get(0);
		return getTypeValue(fieldType, value);
	}

	private Object getTypeValue(CustomFieldTypeEnum fieldType, String value) {
		if (StringUtil.isBlank(value)) {
			return null;
		}
		if (fieldType.getType() == BigDecimal.class) {
			return new BigDecimal(value);
		}
		if (fieldType.getType() == boolean.class) {
			return "true".equals(value) ? true : false;
		}
		if (fieldType.getType() == int.class) {
			return StringUtil.toInt(value);
		}
		if (CustomFieldTypeEnum.DATETIME.equals(fieldType)) {
			return DateUtil.toDateTime(value);
		}
		if (CustomFieldTypeEnum.DATE.equals(fieldType)) {
			return DateUtil.toDate(value);
		}
		if (CustomFieldTypeEnum.ENTITY.equals(fieldType)) {
			return value;
		}
		return fieldType.getType().cast(value);
	}

}
