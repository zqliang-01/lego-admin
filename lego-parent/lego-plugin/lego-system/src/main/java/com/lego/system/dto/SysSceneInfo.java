package com.lego.system.dto;

import com.lego.core.dto.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysSceneInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String data;
	private String formCode;
	private boolean current;
    private boolean enable;
}
