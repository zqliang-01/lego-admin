package com.lego.sharding.mapper;

import java.util.List;

public interface ShardingConfigMapper {

    List<Long> selectValid(String code);
}
