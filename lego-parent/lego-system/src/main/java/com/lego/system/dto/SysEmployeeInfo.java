package com.lego.system.dto;

import java.util.Date;
import java.util.List;

import com.lego.core.common.Constants;
import com.lego.core.dto.TypeInfo;
import com.lego.core.dto.VersionDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysEmployeeInfo extends VersionDTO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String defaultLang = Constants.DEFAULT_LANG;
	private TypeInfo dept;
	private Date createTime;
	private boolean enable;
	private List<TypeInfo> roles;
}
