package com.lego.sharding.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.TypeInfo;
import com.lego.sharding.action.ModifyShardingPropertiesAction;
import com.lego.sharding.assembler.ShardingPropertiesAssembler;
import com.lego.sharding.dao.IShardingPropertiesDao;
import com.lego.sharding.entity.ShardingProperties;
import com.lego.sharding.service.IShardingPropertiesService;
import com.lego.sharding.vo.ShardingPropertiesCreateVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShardingPropertiesService extends BaseService<IShardingPropertiesDao, ShardingPropertiesAssembler> implements IShardingPropertiesService {

    @Override
    public List<TypeInfo> findBy(Long entityId) {
        List<ShardingProperties> properties = dao.findBy(entityId);
        return assembler.createTypeInfo(properties);
    }

    @Override
    public void add(String operatorCode, ShardingPropertiesCreateVO vo) {
        new ModifyShardingPropertiesAction(operatorCode, vo).run();
    }

}
