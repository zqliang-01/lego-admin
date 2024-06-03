package com.lego.sharding.mapper;

import java.util.List;

import com.lego.sharding.dto.config.ShardingMetaAlgorithmInfo;

public interface ShardingAlgorithmMapper {

	List<ShardingMetaAlgorithmInfo> selectValidBy(Long configId);
}
