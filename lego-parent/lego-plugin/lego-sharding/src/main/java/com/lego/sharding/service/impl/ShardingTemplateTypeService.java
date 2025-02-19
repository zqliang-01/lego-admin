package com.lego.sharding.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.sharding.assembler.ShardingTemplateTypeAssembler;
import com.lego.sharding.dao.IShardingTemplateTypeDao;
import com.lego.sharding.dto.ShardingTemplateTypeInfo;
import com.lego.sharding.entity.ShardingTemplateType;
import com.lego.sharding.service.IShardingTemplateTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShardingTemplateTypeService extends BaseService<IShardingTemplateTypeDao, ShardingTemplateTypeAssembler> implements IShardingTemplateTypeService {

    @Override
    public List<ShardingTemplateTypeInfo> findValid() {
        GenericConditionVO conditionVO = GenericConditionVO.create();
        conditionVO.addItem(GenericConditionItemVO.createEqual("enable", true));
        List<ShardingTemplateType> types = dao.findBy(conditionVO);
        return assembler.create(types);
    }

}
