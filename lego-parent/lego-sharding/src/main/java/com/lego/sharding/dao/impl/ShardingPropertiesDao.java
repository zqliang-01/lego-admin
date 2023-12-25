package com.lego.sharding.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingPropertiesDao;
import com.lego.sharding.entity.ShardingProperties;

public class ShardingPropertiesDao extends GenericDao<ShardingProperties> implements IShardingPropertiesDao {

	@Override
	public List<ShardingProperties> findBy(String entityCode, String templateCode, String configCode) {
		QueryHandler<ShardingProperties> query = createQueryHandler();
		query.condition("t.entityCode = :entityCode").param("entityCode", entityCode);
		query.condition("t.template.code = :templateCode").param("templateCode", templateCode);
		if (StringUtil.isNotBlank(configCode)) {
			query.condition("t.config.code = :configCode").param("configCode", configCode);
		}
		return query.findList();
	}

}
