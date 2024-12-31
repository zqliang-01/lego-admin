package com.lego.core.vo;

import com.lego.core.enums.FieldTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GenericSearchItemVO extends VO {

	private static final long serialVersionUID = 1L;

	private String type;
	private String fieldCode;
	private String formType;
	private List<String> values = new ArrayList<String>();

	public List<Object> getValue(FieldTypeEnum fieldType) {
		List<Object> results = new ArrayList<Object>();
		for (String value : values) {
			results.add(fieldType.parseSearchValue(value));
		}
		return results;
	}

	public Object getFirstValue(FieldTypeEnum fieldType) {
		if (values.isEmpty()) {
			return null;
		}
		String value = values.get(0);
		return fieldType.parseSearchValue(value);
	}

}
