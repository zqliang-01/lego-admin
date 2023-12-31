package com.lego.system.vo;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysPermissionCreateVO extends VO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String typeCode;
	private String parentCode;
	private int sn;
}
