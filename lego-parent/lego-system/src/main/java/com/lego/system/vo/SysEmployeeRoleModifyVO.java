package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysEmployeeRoleModifyVO extends VO {

	private static final long serialVersionUID = 1L;

	private String action;
	private String roleCode;
	private List<String> employeeCodes;
}
