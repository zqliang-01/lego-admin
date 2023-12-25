package com.lego.sharding.vo;

import java.util.HashMap;
import java.util.Map;

import com.lego.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingPropertiesCreateVO extends VO {

	private static final long serialVersionUID = 1L;

	private String configCode;
	private String entityCode;
	private String templateCode;
	private Map<String, String> properties = new HashMap<String, String>();
}
