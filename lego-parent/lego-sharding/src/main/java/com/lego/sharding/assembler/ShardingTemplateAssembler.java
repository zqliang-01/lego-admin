package com.lego.sharding.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.sharding.dto.ShardingTemplateInfo;
import com.lego.sharding.entity.ShardingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShardingTemplateAssembler extends EntityAssembler<ShardingTemplateInfo, ShardingTemplate> {

    @Override
    protected ShardingTemplateInfo doCreate(ShardingTemplate entity) {
        ShardingTemplateInfo info = new ShardingTemplateInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setEnable(entity.isEnable());
        info.setDescription(entity.getDescription());
        info.setJson(entity.getJson());
        info.setType(createTypeInfo(entity.getType()));
        return info;
    }
}
