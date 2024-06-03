package com.lego.system.vo;

import java.util.ArrayList;
import java.util.List;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysPermissionAuthVO extends VO {

	private static final long serialVersionUID = 1L;

	private String roleCode;
	private List<String> permissionCodes = new ArrayList<String>();
}
