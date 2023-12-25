package com.lego.system.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysGenTableInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String path;
	private String comment;
	private String appCode;
	private String urlName;
	private String fieldName;
	private String className;
	private String packageName;
	private String permissionCode;
	private TypeInfo creator;
}
