package com.lego.system.vo;

import java.util.List;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysEmployeeCreateVO extends VO {

	private static final long serialVersionUID = 1L;

	private String name;
	private String password;
	private String dept;
	private boolean enable;
	private List<String> roles;
}
