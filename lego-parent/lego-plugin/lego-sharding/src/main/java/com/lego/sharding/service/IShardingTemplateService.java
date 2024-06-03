package com.lego.sharding.service;

import java.util.List;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.sharding.dto.ShardingTemplateInfo;
import com.lego.sharding.vo.ShardingTemplateCreateVO;
import com.lego.sharding.vo.ShardingTemplateModifyVO;

public interface IShardingTemplateService {

    LegoPage<ShardingTemplateInfo> findPageBy(GenericSearchVO vo);

    ShardingTemplateInfo findBy(String code);

    String findJsonBy(String code);

	List<TypeInfo> findSimpleTypeBy(String typeCode);

    List<ShardingTemplateInfo> findBy(GenericSearchVO vo);

    List<ShardingTemplateInfo> findBy(List<String> codes);

    void update(String operatorCode, ShardingTemplateModifyVO vo);

    void add(String operatorCode, ShardingTemplateCreateVO vo);

    void delete(String operatorCode, List<String> codes);
}
