package com.lego.system.vo;

import java.util.ArrayList;
import java.util.List;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysSceneVisibleVO extends VO {

	private static final long serialVersionUID = 1L;

	private List<String> codes = new ArrayList<String>();
	private List<String> hiddenCodes = new ArrayList<String>();
}
