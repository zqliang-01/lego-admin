package com.lego.sharding.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.core.vo.GenericSearchVO;
import com.lego.sharding.action.AddShardingConfigAction;
import com.lego.sharding.action.DeleteShardingConfigAction;
import com.lego.sharding.action.ModifyShardingConfigAction;
import com.lego.sharding.assembler.ShardingConfigAssembler;
import com.lego.sharding.dao.IShardingConfigDao;
import com.lego.sharding.dto.ShardingConfigInfo;
import com.lego.sharding.entity.ShardingConfig;
import com.lego.sharding.service.IShardingConfigService;
import com.lego.sharding.vo.ShardingConfigCreateVO;
import com.lego.sharding.vo.ShardingConfigModifyVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShardingConfigService extends BaseService<IShardingConfigDao, ShardingConfigAssembler> implements IShardingConfigService {

    @Override
    public LegoPage<ShardingConfigInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<ShardingConfig> configs = dao.findPageBy(buildCondition(vo));
        return assembler.create(configs);
    }

    @Override
    public ShardingConfigInfo findBy(String code) {
        ShardingConfig config = dao.findByCode(code);
        return assembler.create(config);
    }

    @Override
    public List<ShardingConfigInfo> findBy(GenericSearchVO vo) {
        List<ShardingConfig> configs = dao.findBy(buildCondition(vo));
        return assembler.create(configs);
    }

    @Override
    public List<ShardingConfigInfo> findBy(List<String> codes) {
        List<ShardingConfig> configs = dao.findByCodes(codes);
        return assembler.create(configs);
    }

    @Override
    public void update(String operatorCode, ShardingConfigModifyVO vo) {
        new ModifyShardingConfigAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, ShardingConfigCreateVO vo) {
        new AddShardingConfigAction(operatorCode, vo).run();
    }

    @Override
    public void delete(String operatorCode, List<String> codes) {
        for (String code : codes) {
            new DeleteShardingConfigAction(operatorCode, code).run();
        }
    }

    @Override
    public List<TypeInfo> findSimpleType() {
        GenericConditionVO conditionVO = GenericConditionVO.create();
        conditionVO.addItem(GenericConditionItemVO.createEqual("enable", true));
        List<ShardingConfig> templates = dao.findBy(conditionVO);
        return assembler.createTypeInfo(templates);
    }
}
