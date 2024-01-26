package com.lego.sharding.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.sharding.dto.ShardingPropertiesInfo;
import com.lego.sharding.entity.ShardingProperties;
import org.springframework.stereotype.Component;

@Component
public class ShardingPropertiesAssembler extends EntityAssembler<ShardingPropertiesInfo, ShardingProperties> {

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
