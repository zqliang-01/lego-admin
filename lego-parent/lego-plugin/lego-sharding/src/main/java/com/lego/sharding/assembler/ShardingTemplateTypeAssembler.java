package com.lego.sharding.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.sharding.dto.ShardingTemplateTypeInfo;
import com.lego.sharding.entity.ShardingTemplateType;
import org.springframework.stereotype.Component;

@Component
public class ShardingTemplateTypeAssembler extends EntityAssembler<ShardingTemplateTypeInfo, ShardingTemplateType> {

    @Override
    protected ShardingTemplateTypeInfo doCreate(ShardingTemplateType entity) {
        ShardingTemplateTypeInfo info = new ShardingTemplateTypeInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setDescription(entity.getDescription());
        return info;
    }
}
