package com.lego.sharding.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.sharding.dto.ShardingTableInfo;
import com.lego.sharding.entity.ShardingTable;

@Component
public class ShardingTableAssembler extends BaseAssembler<ShardingTableInfo, ShardingTable> {

    @Override
    protected ShardingTableInfo doCreate(ShardingTable entity) {
        ShardingTableInfo info = new ShardingTableInfo();
    	info.setCode(entity.getCode());
    	info.setName(entity.getName());
    	info.setEnable(entity.isEnable());
    	info.setDescription(entity.getDescription());
    	info.setLogicTableName(entity.getLogicTableName());
    	info.setActualDataNodes(entity.getActualDataNodes());
    	info.setShardingColumn(entity.getShardingColumn());
    	info.setAlgorithm(createTypeInfo(entity.getAlgorithm()));
    	info.setTemplate(createTypeInfo(entity.getTemplate()));
    	info.setConfig(createTypeInfo(entity.getConfig()));
        return info;
    }
}
