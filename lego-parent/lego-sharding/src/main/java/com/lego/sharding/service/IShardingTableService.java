package com.lego.sharding.service;

import java.util.List;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericSearchVO;
import com.lego.sharding.dto.ShardingTableInfo;
import com.lego.sharding.vo.ShardingTableCreateVO;
import com.lego.sharding.vo.ShardingTableModifyVO;

public interface IShardingTableService {

    LegoPage<ShardingTableInfo> findPageBy(GenericSearchVO vo);

    ShardingTableInfo findBy(String code);

    List<ShardingTableInfo> findBy(GenericSearchVO vo);

    List<ShardingTableInfo> findBy(List<String> codes);

    void update(String operatorCode, ShardingTableModifyVO vo);

    void add(String operatorCode, ShardingTableCreateVO vo);

    void delete(String operatorCode, List<String> codes);
}
