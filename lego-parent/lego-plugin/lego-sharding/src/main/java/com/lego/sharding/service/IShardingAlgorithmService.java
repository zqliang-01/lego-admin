package com.lego.sharding.service;

import java.util.List;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.sharding.dto.ShardingAlgorithmInfo;
import com.lego.sharding.vo.ShardingAlgorithmCreateVO;
import com.lego.sharding.vo.ShardingAlgorithmModifyVO;

public interface IShardingAlgorithmService {

    LegoPage<ShardingAlgorithmInfo> findPageBy(GenericSearchVO vo);

    ShardingAlgorithmInfo findBy(String code);

    List<ShardingAlgorithmInfo> findBy(GenericSearchVO vo);

    List<ShardingAlgorithmInfo> findBy(List<String> codes);

    void update(String operatorCode, ShardingAlgorithmModifyVO vo);

    void add(String operatorCode, ShardingAlgorithmCreateVO vo);

    void delete(String operatorCode, List<String> codes);

	List<TypeInfo> findSimpleType();
}
