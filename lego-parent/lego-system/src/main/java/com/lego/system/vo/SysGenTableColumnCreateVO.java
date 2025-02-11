package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SysGenTableColumnCreateVO extends VO {

	private static final long serialVersionUID = 1L;

	private int sn;
	private String name;
	private String comment;
	private String dataType;
	private String formType;
	private String javaField;
	private String javaFieldType;
	private String relativeTable;
	private boolean required;
	private boolean unique;
	private String tableCode;
	private Map<String, String> attributes;
}
