package com.lego.sharding.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.sharding.entity.ShardingProperties;

public interface IShardingPropertiesDao extends IGenericDao<ShardingProperties> {

	List<ShardingProperties> findBy(String entityCode, String templateCode, String configCode);
}
