package com.lego.sharding.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BusiService;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.sharding.assembler.ShardingTemplateTypeAssembler;
import com.lego.sharding.dao.IShardingTemplateTypeDao;
import com.lego.sharding.dto.ShardingTemplateTypeInfo;
import com.lego.sharding.entity.ShardingTemplateType;
import com.lego.sharding.service.IShardingTemplateTypeService;

@Service
public class ShardingTemplateTypeService extends BusiService<IShardingTemplateTypeDao, ShardingTemplateTypeAssembler> implements IShardingTemplateTypeService {

	@Override
	public List<ShardingTemplateTypeInfo> findValid() {
		GenericConditionVO conditionVO = GenericConditionVO.create();
		conditionVO.addItem(GenericConditionItemVO.createEqual("enable", true));
		List<ShardingTemplateType> types = dao.findBy(conditionVO);
		return assembler.create(types);
	}

}
