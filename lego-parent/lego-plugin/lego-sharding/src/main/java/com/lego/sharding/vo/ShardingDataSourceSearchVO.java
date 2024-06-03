package com.lego.sharding.vo;

import com.lego.core.vo.PageVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingDataSourceSearchVO extends PageVO {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
}
