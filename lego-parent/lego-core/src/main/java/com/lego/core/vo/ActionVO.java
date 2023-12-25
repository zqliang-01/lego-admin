package com.lego.core.vo;

import com.lego.core.data.ActionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionVO extends VO {

	private static final long serialVersionUID = 1L;

	private String entityCode;
	private String operatorCode;
	private String permissionCode;
	private ActionType actionType;
	private String description;
}
