package com.lego.system.vo;

import java.util.ArrayList;
import java.util.List;

import com.lego.core.util.StringUtil;
import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysCustomFormFieldModifyVO extends VO {

	private static final long serialVersionUID = 1L;

	private String formCode;
	private List<SysCustomFieldModifyVO> data = new ArrayList<SysCustomFieldModifyVO>();

	public SysCustomFieldModifyVO getFieldVO(String code) {
		for (SysCustomFieldModifyVO field : data) {
			if (StringUtil.equals(field.getCode(), code)) {
				return field;
			}
		}
		return null;
	}
}
