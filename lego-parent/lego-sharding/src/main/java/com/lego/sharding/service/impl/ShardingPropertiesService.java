package com.lego.sharding.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BusiService;
import com.lego.core.dto.TypeInfo;
import com.lego.sharding.action.ModifyShardingPropertiesAction;
import com.lego.sharding.assembler.ShardingPropertiesAssembler;
import com.lego.sharding.dao.IShardingPropertiesDao;
import com.lego.sharding.entity.ShardingProperties;
import com.lego.sharding.service.IShardingPropertiesService;
import com.lego.sharding.vo.ShardingPropertiesCreateVO;

@Service
public class ShardingPropertiesService extends BusiService<IShardingPropertiesDao, ShardingPropertiesAssembler> implements IShardingPropertiesService {

	@Override
	public List<TypeInfo> findBy(String entityCode, String templateCode, String configCode) {
		List<ShardingProperties> properties = dao.findBy(entityCode, templateCode, configCode);
		return assembler.createTypeInfo(properties);
	}

	@Override
	public void add(String operatorCode, ShardingPropertiesCreateVO vo) {
		new ModifyShardingPropertiesAction(operatorCode, vo).run();
	}

}
