package com.lego.system.dto;

import com.lego.core.dto.TreeDTO;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysCodePreviewInfo extends TreeDTO<SysCodePreviewInfo> {

	private static final long serialVersionUID = 1L;

	private String value;

	public SysCodePreviewInfo(String code) {
		this.setCode(code);
	}

	public SysCodePreviewInfo getChildren(String code) {
		for (SysCodePreviewInfo children : getChildrens()) {
			if (StringUtil.equals(children.getCode(), code)) {
				return children;
			}
		}
		return null;
	}
}
