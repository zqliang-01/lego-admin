package com.lego.sharding.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.sharding.dao.IShardingPropertiesDao;
import com.lego.sharding.entity.ShardingProperties;

import java.util.List;

public class ShardingPropertiesDao extends GenericDao<ShardingProperties> implements IShardingPropertiesDao {

    @Override
    public List<ShardingProperties> findBy(Long entityId) {
        QueryHandler<ShardingProperties> query = createQueryHandler();
        query.condition("t.entityId = :entityId").param("entityId", entityId);
        return query.findList();
    }

}
