package com.lego.system.vo;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysGenTableColumnModifyVO extends VO {

	private static final long serialVersionUID = 1L;

	private int sn;
	private String code;
	private String name;
	private String comment;
	private String dataType;
	private String formType;
	private String javaField;
	private String javaFieldType;
	private String relativeTableCode;
	private boolean required;
	private boolean unique;
}
