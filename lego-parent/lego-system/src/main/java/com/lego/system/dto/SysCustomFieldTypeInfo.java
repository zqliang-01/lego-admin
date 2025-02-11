package com.lego.system.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysCustomFieldTypeInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String type;

	public SysCustomFieldTypeInfo(String code, String name, String type) {
		this.code = code;
		this.name = name;
		this.type = type;
	}
}
