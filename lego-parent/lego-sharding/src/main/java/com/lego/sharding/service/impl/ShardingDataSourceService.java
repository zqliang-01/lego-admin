package com.lego.sharding.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BusiService;
import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericSearchVO;
import com.lego.sharding.action.AddShardingDataSourceAction;
import com.lego.sharding.action.DeleteShardingDataSourceAction;
import com.lego.sharding.action.ModifyShardingDataSourceAction;
import com.lego.sharding.assembler.ShardingDataSourceAssembler;
import com.lego.sharding.dao.IShardingDataSourceDao;
import com.lego.sharding.dto.ShardingDataSourceInfo;
import com.lego.sharding.entity.ShardingDataSource;
import com.lego.sharding.service.IShardingDataSourceService;
import com.lego.sharding.vo.ShardingDataSourceCreateVO;
import com.lego.sharding.vo.ShardingDataSourceModifyVO;

@Service
public class ShardingDataSourceService extends BusiService<IShardingDataSourceDao, ShardingDataSourceAssembler> implements IShardingDataSourceService {

    @Override
    public LegoPage<ShardingDataSourceInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<ShardingDataSource> dataSources = dao.findPageBy(buildCondition(vo));
        return assembler.create(dataSources);
    }

    @Override
    public ShardingDataSourceInfo findBy(String code) {
        ShardingDataSource dataSource = dao.findByCode(code);
        return assembler.create(dataSource);
    }

    @Override
    public List<ShardingDataSourceInfo> findBy(GenericSearchVO vo) {
        List<ShardingDataSource> dataSources = dao.findBy(buildCondition(vo));
        return assembler.create(dataSources);
    }

    @Override
    public List<ShardingDataSourceInfo> findBy(List<String> codes) {
        List<ShardingDataSource> dataSources = dao.findByCodes(codes);
        return assembler.create(dataSources);
    }

    @Override
    public void update(String operatorCode, ShardingDataSourceModifyVO vo) {
        new ModifyShardingDataSourceAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, ShardingDataSourceCreateVO vo) {
        new AddShardingDataSourceAction(operatorCode, vo).run();
    }

    @Override
    public void delete(String operatorCode, List<String> codes) {
        for (String code : codes) {
            new DeleteShardingDataSourceAction(operatorCode, code).run();
        }
    }
}
