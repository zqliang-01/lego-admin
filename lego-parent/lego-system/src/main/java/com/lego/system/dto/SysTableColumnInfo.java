package com.lego.system.dto;

import java.util.List;

import com.lego.core.dto.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysTableColumnInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String formCode;
	private String permissionCode;
	private List<SysCustomFieldInfo> fields;

	public SysTableColumnInfo(String formCode, String permissionCode, List<SysCustomFieldInfo> fields) {
		this.formCode = formCode;
		this.permissionCode = permissionCode;
		this.fields = fields;
	}
}
