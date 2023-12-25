package com.lego.system.dto;

import java.util.Date;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysFileInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private long size;
	private TypeInfo creator;
	private TypeInfo type;
	private Date createTime;
}
