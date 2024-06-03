package com.lego.sharding.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.sharding.dto.ShardingDataSourceInfo;
import com.lego.sharding.entity.ShardingDataSource;
import org.springframework.stereotype.Component;

@Component
public class ShardingDataSourceAssembler extends EntityAssembler<ShardingDataSourceInfo, ShardingDataSource> {

    @Override
    protected ShardingDataSourceInfo doCreate(ShardingDataSource entity) {
        ShardingDataSourceInfo info = new ShardingDataSourceInfo();
        info.setId(entity.getId());
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setEnable(entity.isEnable());
        info.setDescription(entity.getDescription());
        info.setTemplate(createTypeInfo(entity.getTemplate()));
        return info;
    }
}
