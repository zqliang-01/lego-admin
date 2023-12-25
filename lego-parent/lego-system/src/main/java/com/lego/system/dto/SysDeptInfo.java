package com.lego.system.dto;

import com.lego.core.dto.TreeDTO;
import com.lego.core.dto.TypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysDeptInfo extends TreeDTO<SysDeptInfo> {

	private static final long serialVersionUID = 1L;

	private String name;
	private TypeInfo leader;
	private boolean enable;
	private int serialNumber;
}
