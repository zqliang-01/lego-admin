package com.lego.sharding.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.sharding.dto.ShardingAlgorithmInfo;
import com.lego.sharding.entity.ShardingAlgorithm;

@Component
public class ShardingAlgorithmAssembler extends BaseAssembler<ShardingAlgorithmInfo, ShardingAlgorithm> {

    @Override
    protected ShardingAlgorithmInfo doCreate(ShardingAlgorithm entity) {
        ShardingAlgorithmInfo info = new ShardingAlgorithmInfo();
    	info.setCode(entity.getCode());
    	info.setName(entity.getName());
    	info.setEnable(entity.isEnable());
    	info.setDescription(entity.getDescription());
    	info.setTemplate(createTypeInfo(entity.getTemplate()));
    	info.setConfig(createTypeInfo(entity.getConfig()));
        return info;
    }
}
