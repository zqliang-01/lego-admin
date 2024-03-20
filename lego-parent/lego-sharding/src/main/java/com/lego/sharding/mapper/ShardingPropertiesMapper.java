package com.lego.sharding.mapper;

import com.lego.core.dto.TypeInfo;

import java.util.List;

public interface ShardingPropertiesMapper {

    List<TypeInfo> selectValidBy(Long entityId, String templateCode, String configCode);

}
