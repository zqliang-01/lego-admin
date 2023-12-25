package com.lego.core.vo;

import java.util.ArrayList;
import java.util.List;

import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericConditionVO extends PageVO {

	private static final long serialVersionUID = 1L;

	private String orderType;
	private List<GenericConditionItemVO> items = new ArrayList<GenericConditionItemVO>();

	private GenericConditionVO() { }

	public static GenericConditionVO create() {
		return new GenericConditionVO();
	}

	public static GenericConditionVO create(PageVO pageVO) {
		return new GenericConditionVO(pageVO);
	}

	private GenericConditionVO(PageVO pageVO) {
		this.setPageSize(pageVO.getPageSize());
		this.setPageIndex(pageVO.getPageIndex());
	}

	public GenericConditionVO addItem(GenericConditionItemVO item) {
		boolean containItem = items.stream().anyMatch(c -> StringUtil.equals(c.getKey(), item.getKey()));
		if (containItem) {
			item.setIndex(items.size());
		}
		this.items.add(item);
		return this;
	}
}
