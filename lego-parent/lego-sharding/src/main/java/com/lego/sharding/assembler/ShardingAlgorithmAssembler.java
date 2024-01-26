package com.lego.sharding.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.sharding.dto.ShardingAlgorithmInfo;
import com.lego.sharding.entity.ShardingAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class ShardingAlgorithmAssembler extends EntityAssembler<ShardingAlgorithmInfo, ShardingAlgorithm> {

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
