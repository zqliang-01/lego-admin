package com.lego.system.vo;

import java.util.List;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysEmployeeModifyVO extends VO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private boolean enable;
	private String password;
	private String dept;
	private List<String> roles;
}
