package com.lego.sharding.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.sharding.dto.ShardingDataSourceInfo;
import com.lego.sharding.entity.ShardingDataSource;

@Component
public class ShardingDataSourceAssembler extends BaseAssembler<ShardingDataSourceInfo, ShardingDataSource> {

    @Override
    protected ShardingDataSourceInfo doCreate(ShardingDataSource entity) {
        ShardingDataSourceInfo info = new ShardingDataSourceInfo();
    	info.setCode(entity.getCode());
    	info.setName(entity.getName());
    	info.setEnable(entity.isEnable());
    	info.setDescription(entity.getDescription());
    	info.setTemplate(createTypeInfo(entity.getTemplate()));
        return info;
    }
}
