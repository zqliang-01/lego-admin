package com.lego.system.dto;

import java.util.Date;

import com.lego.core.dto.TreeDTO;
import com.lego.core.dto.TypeInfo;
import com.lego.system.vo.SysPermissionTypeCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysPermissionInfo extends TreeDTO<SysPermissionInfo> {

	private static final long serialVersionUID = 1L;

	private String name;
	private String icon;
	private TypeInfo type;
	private Date createTime;

	public String getRealm() {
		String[] codes = getCode().split(":");
		return codes[codes.length - 1];
	}

	public boolean isApp() {
		return type != null && SysPermissionTypeCode.APP.equals(type.getCode());
	}
}
