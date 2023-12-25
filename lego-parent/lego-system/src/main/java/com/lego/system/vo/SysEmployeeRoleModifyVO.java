package com.lego.system.vo;

import java.util.List;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysEmployeeRoleModifyVO extends VO {

	private static final long serialVersionUID = 1L;

	private String code;
	private boolean cleanable;
	private List<String> codes;
}
