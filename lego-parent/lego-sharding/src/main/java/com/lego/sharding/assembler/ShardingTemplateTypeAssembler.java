package com.lego.sharding.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.sharding.dto.ShardingTemplateTypeInfo;
import com.lego.sharding.entity.ShardingTemplateType;

@Component
public class ShardingTemplateTypeAssembler extends BaseAssembler<ShardingTemplateTypeInfo, ShardingTemplateType> {

	@Override
	protected ShardingTemplateTypeInfo doCreate(ShardingTemplateType entity) {
		ShardingTemplateTypeInfo info = new ShardingTemplateTypeInfo();
		info.setCode(entity.getCode());
		info.setName(entity.getName());
		info.setDescription(entity.getDescription());
		return info;
	}
}
