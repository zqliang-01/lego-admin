package com.lego.sharding.mapper;

import com.lego.sharding.dto.config.ShardingMetaDataSourceInfo;

import java.util.List;

public interface ShardingDataSourceMapper {

    List<ShardingMetaDataSourceInfo> selectValid();
}
