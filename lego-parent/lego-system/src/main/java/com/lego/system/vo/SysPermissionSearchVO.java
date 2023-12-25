package com.lego.system.vo;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysPermissionSearchVO extends VO {

	private static final long serialVersionUID = -4635128771978113775L;

	private String code;
	private String name;
	private String roleCode;
}
