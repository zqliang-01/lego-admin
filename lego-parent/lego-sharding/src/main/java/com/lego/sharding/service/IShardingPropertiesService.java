package com.lego.sharding.service;

import java.util.List;

import com.lego.core.dto.TypeInfo;
import com.lego.sharding.vo.ShardingPropertiesCreateVO;

public interface IShardingPropertiesService {

	List<TypeInfo> findBy(String entityCode, String templateCode, String configCode);

	void add(String operatorCode, ShardingPropertiesCreateVO vo);
}
