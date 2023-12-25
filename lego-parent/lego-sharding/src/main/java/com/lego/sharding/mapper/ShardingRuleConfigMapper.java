package com.lego.sharding.mapper;

import java.util.List;

public interface ShardingRuleConfigMapper {

	List<Long> selectValid();
}
