package com.lego.sharding.mapper;

import com.lego.sharding.dto.config.ShardingMetaTableInfo;

import java.util.List;

public interface ShardingTableMapper {

    List<ShardingMetaTableInfo> selectValidBy(Long configId);
}
