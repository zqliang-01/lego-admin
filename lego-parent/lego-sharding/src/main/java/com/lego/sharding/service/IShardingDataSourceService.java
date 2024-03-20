package com.lego.sharding.service;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.sharding.dto.ShardingDataSourceInfo;
import com.lego.sharding.vo.ShardingDataSourceCreateVO;
import com.lego.sharding.vo.ShardingDataSourceModifyVO;

import java.util.List;

public interface IShardingDataSourceService {

    LegoPage<ShardingDataSourceInfo> findPageBy(GenericSearchVO vo);

    ShardingDataSourceInfo findBy(String code);

    List<TypeInfo> findSimpleType();

    List<ShardingDataSourceInfo> findBy(GenericSearchVO vo);

    List<ShardingDataSourceInfo> findBy(List<String> codes);

    void update(String operatorCode, ShardingDataSourceModifyVO vo);

    void add(String operatorCode, ShardingDataSourceCreateVO vo);

    void delete(String operatorCode, List<String> codes);
}
