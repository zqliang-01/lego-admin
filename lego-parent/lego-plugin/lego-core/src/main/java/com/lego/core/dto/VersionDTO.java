package com.lego.core.dto;

import com.alibaba.excel.annotation.ExcelIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VersionDTO extends DTO {

	private static final long serialVersionUID = 1L;

	@ExcelIgnore
	private Integer version;
}
