package com.lego.sharding.dto;

import com.lego.core.dto.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingTemplateTypeInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String description;
}
