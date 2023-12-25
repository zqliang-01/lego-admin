package com.lego.core.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericSearchVO extends PageVO {

	private static final long serialVersionUID = 1L;

	private String search;
	private String formCode;
	private String orderType;
	private String sortField;
	private List<GenericSearchItemVO> searchList = new ArrayList<GenericSearchItemVO>();
}
