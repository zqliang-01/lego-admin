package com.lego.system.vo;

import com.lego.core.vo.PageVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysEmployeeSearchVO extends PageVO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String deptCode;
	private String roleCode;
	private Boolean enable;
}
