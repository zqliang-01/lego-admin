package com.lego.system.vo;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysEmployeeCurrentModifyVO extends VO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String imageCode;
}
