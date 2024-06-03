package com.lego.system.dto;

import com.lego.core.dto.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysColumnSortInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private Integer width;
	private boolean visible;
	private int sn;
}
