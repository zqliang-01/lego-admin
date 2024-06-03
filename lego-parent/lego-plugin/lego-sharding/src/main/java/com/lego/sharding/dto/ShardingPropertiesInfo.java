package com.lego.sharding.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingPropertiesInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String description;
	private TypeInfo template;
}
