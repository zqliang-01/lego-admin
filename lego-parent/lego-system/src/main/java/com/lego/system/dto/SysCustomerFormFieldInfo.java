package com.lego.system.dto;

import java.util.List;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysCustomerFormFieldInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String appCode;
	private List<TypeInfo> columnInfos;
	private List<List<SysCustomFieldInfo>> fields;

	public SysCustomerFormFieldInfo(String appCode, List<List<SysCustomFieldInfo>> fields, List<TypeInfo> columnInfos) {
		this.appCode = appCode;
		this.fields = fields;
		this.columnInfos = columnInfos;
	}
}
