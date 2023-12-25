package com.lego.system.vo;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysAppSortModifyVO extends VO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String permissionCode;
}
