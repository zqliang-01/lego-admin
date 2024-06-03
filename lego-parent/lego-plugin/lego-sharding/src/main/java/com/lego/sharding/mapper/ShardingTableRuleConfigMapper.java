package com.lego.sharding.mapper;

import java.util.List;

import com.lego.sharding.dto.config.ShardingMetaTableRuleConfigInfo;

public interface ShardingTableRuleConfigMapper {

	List<ShardingMetaTableRuleConfigInfo> selectValidBy(Long configId);
}
