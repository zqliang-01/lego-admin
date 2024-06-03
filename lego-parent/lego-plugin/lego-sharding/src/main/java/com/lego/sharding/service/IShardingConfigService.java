package com.lego.sharding.service;

import java.util.List;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.sharding.dto.ShardingConfigInfo;
import com.lego.sharding.vo.ShardingConfigCreateVO;
import com.lego.sharding.vo.ShardingConfigModifyVO;

public interface IShardingConfigService {

    LegoPage<ShardingConfigInfo> findPageBy(GenericSearchVO vo);

    ShardingConfigInfo findBy(String code);

    List<ShardingConfigInfo> findBy(GenericSearchVO vo);

    List<ShardingConfigInfo> findBy(List<String> codes);

    void update(String operatorCode, ShardingConfigModifyVO vo);

    void add(String operatorCode, ShardingConfigCreateVO vo);

    void delete(String operatorCode, List<String> codes);

	List<TypeInfo> findSimpleType();
}
