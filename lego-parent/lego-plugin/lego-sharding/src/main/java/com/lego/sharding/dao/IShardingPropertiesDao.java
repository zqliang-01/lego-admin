package com.lego.sharding.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.sharding.entity.ShardingProperties;

import java.util.List;

public interface IShardingPropertiesDao extends IGenericDao<ShardingProperties> {

    List<ShardingProperties> findBy(Long entityId);
}
