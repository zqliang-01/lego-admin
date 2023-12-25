package com.lego.sharding.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.sharding.dto.ShardingPropertiesInfo;
import com.lego.sharding.entity.ShardingProperties;

@Component
public class ShardingPropertiesAssembler extends BaseAssembler<ShardingPropertiesInfo, ShardingProperties> {

	@Override
	protected ShardingPropertiesInfo doCreate(ShardingProperties entity) {
		ShardingPropertiesInfo info = new ShardingPropertiesInfo();
		info.setCode(entity.getCode());
		info.setName(entity.getName());
		info.setDescription(entity.getDescription());
		info.setTemplate(createTypeInfo(entity.getTemplate()));
		return info;
	}
}
