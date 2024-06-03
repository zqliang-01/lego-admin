package com.lego.system.dto;

import com.lego.core.dto.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SysCaptchaImageInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String image;
	private String token;
}
