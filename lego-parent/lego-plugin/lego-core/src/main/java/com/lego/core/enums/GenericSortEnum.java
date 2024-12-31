package com.lego.core.enums;

import lombok.Getter;

@Getter
public enum GenericSortEnum {

    ASC("ascending", "升序", "ASC"),
    DESC("descending", "降序", "DESC");

	private String code;
	private String name;
	private String type;

	private GenericSortEnum(String code, String name, String type) {
		this.code = code;
		this.name = name;
		this.type = type;
	}

	public boolean equals(String code) {
		return this.getCode().equals(code);
	}

	public static String getSortType(String code) {
		for (GenericSortEnum value : values()) {
			if (value.equals(code)) {
				return value.getType();
			}
		}
		return null;
	}
}
