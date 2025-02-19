package com.lego.sharding.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericSearchVO;
import com.lego.sharding.action.AddShardingTableAction;
import com.lego.sharding.action.DeleteShardingTableAction;
import com.lego.sharding.action.ModifyShardingTableAction;
import com.lego.sharding.assembler.ShardingTableAssembler;
import com.lego.sharding.dao.IShardingTableDao;
import com.lego.sharding.dto.ShardingTableInfo;
import com.lego.sharding.entity.ShardingTable;
import com.lego.sharding.service.IShardingTableService;
import com.lego.sharding.vo.ShardingTableCreateVO;
import com.lego.sharding.vo.ShardingTableModifyVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShardingTableService extends BaseService<IShardingTableDao, ShardingTableAssembler> implements IShardingTableService {

    @Override
    public LegoPage<ShardingTableInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<ShardingTable> tables = dao.findPageBy(buildCondition(vo));
        return assembler.create(tables);
    }

    @Override
    public ShardingTableInfo findBy(String code) {
        ShardingTable table = dao.findByCode(code);
        return assembler.create(table);
    }

    @Override
    public List<ShardingTableInfo> findBy(GenericSearchVO vo) {
        List<ShardingTable> tables = dao.findBy(buildCondition(vo));
        return assembler.create(tables);
    }

    @Override
    public List<ShardingTableInfo> findBy(List<String> codes) {
        List<ShardingTable> tables = dao.findByCodes(codes);
        return assembler.create(tables);
    }

    @Override
    public void update(String operatorCode, ShardingTableModifyVO vo) {
        new ModifyShardingTableAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, ShardingTableCreateVO vo) {
        new AddShardingTableAction(operatorCode, vo).run();
    }

    @Override
    public void delete(String operatorCode, List<String> codes) {
        for (String code : codes) {
            new DeleteShardingTableAction(operatorCode, code).run();
        }
    }
}
