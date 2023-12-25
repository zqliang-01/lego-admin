package com.lego.sharding.mapper;

import java.util.List;

import com.lego.core.dto.TypeInfo;

public interface ShardingPropertiesMapper {

	List<TypeInfo> selectValidBy(String entityCode, String templateCode, String configCode);

}
