package com.lego.sharding.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.sharding.dto.ShardingConfigInfo;
import com.lego.sharding.entity.ShardingConfig;
import org.springframework.stereotype.Component;

@Component
public class ShardingConfigAssembler extends EntityAssembler<ShardingConfigInfo, ShardingConfig> {

    @Override
    protected ShardingConfigInfo doCreate(ShardingConfig entity) {
        ShardingConfigInfo info = new ShardingConfigInfo();
        info.setId(entity.getId());
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setEnable(entity.isEnable());
        info.setDescription(entity.getDescription());
        return info;
    }
}
