package com.lego.sharding.service;

import com.lego.core.dto.TypeInfo;
import com.lego.sharding.vo.ShardingPropertiesCreateVO;

import java.util.List;

public interface IShardingPropertiesService {

    List<TypeInfo> findBy(Long entityId);

    void add(String operatorCode, ShardingPropertiesCreateVO vo);
}
