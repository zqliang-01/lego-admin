package com.lego.sharding.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.sharding.dto.ShardingConfigInfo;
import com.lego.sharding.entity.ShardingConfig;

@Component
public class ShardingConfigAssembler extends BaseAssembler<ShardingConfigInfo, ShardingConfig> {

    @Override
    protected ShardingConfigInfo doCreate(ShardingConfig entity) {
        ShardingConfigInfo info = new ShardingConfigInfo();
    	info.setCode(entity.getCode());
    	info.setName(entity.getName());
    	info.setEnable(entity.isEnable());
    	info.setDescription(entity.getDescription());
        return info;
    }
}
