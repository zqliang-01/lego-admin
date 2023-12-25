package com.lego.sharding.service;

import java.util.List;

import com.lego.sharding.dto.ShardingTemplateTypeInfo;

public interface IShardingTemplateTypeService {

	List<ShardingTemplateTypeInfo> findValid();
}
