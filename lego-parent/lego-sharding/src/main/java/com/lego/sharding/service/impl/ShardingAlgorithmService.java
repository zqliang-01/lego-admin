package com.lego.sharding.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.core.vo.GenericSearchVO;
import com.lego.sharding.action.AddShardingAlgorithmAction;
import com.lego.sharding.action.DeleteShardingAlgorithmAction;
import com.lego.sharding.action.ModifyShardingAlgorithmAction;
import com.lego.sharding.assembler.ShardingAlgorithmAssembler;
import com.lego.sharding.dao.IShardingAlgorithmDao;
import com.lego.sharding.dto.ShardingAlgorithmInfo;
import com.lego.sharding.entity.ShardingAlgorithm;
import com.lego.sharding.service.IShardingAlgorithmService;
import com.lego.sharding.vo.ShardingAlgorithmCreateVO;
import com.lego.sharding.vo.ShardingAlgorithmModifyVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShardingAlgorithmService extends BusService<IShardingAlgorithmDao, ShardingAlgorithmAssembler> implements IShardingAlgorithmService {

    @Override
    public LegoPage<ShardingAlgorithmInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<ShardingAlgorithm> algorithms = dao.findPageBy(buildCondition(vo));
        return assembler.create(algorithms);
    }

    @Override
    public ShardingAlgorithmInfo findBy(String code) {
        ShardingAlgorithm algorithm = dao.findByCode(code);
        return assembler.create(algorithm);
    }

    @Override
    public List<ShardingAlgorithmInfo> findBy(GenericSearchVO vo) {
        List<ShardingAlgorithm> algorithms = dao.findBy(buildCondition(vo));
        return assembler.create(algorithms);
    }

    @Override
    public List<ShardingAlgorithmInfo> findBy(List<String> codes) {
        List<ShardingAlgorithm> algorithms = dao.findByCodes(codes);
        return assembler.create(algorithms);
    }

    @Override
    public void update(String operatorCode, ShardingAlgorithmModifyVO vo) {
        new ModifyShardingAlgorithmAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, ShardingAlgorithmCreateVO vo) {
        new AddShardingAlgorithmAction(operatorCode, vo).run();
    }

    @Override
    public void delete(String operatorCode, List<String> codes) {
        for (String code : codes) {
            new DeleteShardingAlgorithmAction(operatorCode, code).run();
        }
    }

    @Override
    public List<TypeInfo> findSimpleType() {
        GenericConditionVO conditionVO = GenericConditionVO.create();
        conditionVO.addItem(GenericConditionItemVO.createEqual("enable", true));
        List<ShardingAlgorithm> templates = dao.findBy(conditionVO);
        return assembler.createTypeInfo(templates);
    }
}
