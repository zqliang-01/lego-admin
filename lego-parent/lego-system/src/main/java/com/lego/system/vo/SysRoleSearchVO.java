package com.lego.system.vo;

import com.lego.core.vo.PageVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysRoleSearchVO extends PageVO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
}
