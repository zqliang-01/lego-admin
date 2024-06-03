package com.lego.system.vo;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysCodeGeneratorCreateVO extends VO {

	private static final long serialVersionUID = 1L;

	private String name;
	private String prefix;
	private int serialLength;
	private String datePattern;
	private String customFieldCode;
}
