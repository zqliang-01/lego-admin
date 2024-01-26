package com.lego.sharding.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.core.vo.GenericSearchVO;
import com.lego.sharding.action.AddShardingTemplateAction;
import com.lego.sharding.action.DeleteShardingTemplateAction;
import com.lego.sharding.action.ModifyShardingTemplateAction;
import com.lego.sharding.assembler.ShardingTemplateAssembler;
import com.lego.sharding.dao.IShardingTemplateDao;
import com.lego.sharding.dto.ShardingTemplateInfo;
import com.lego.sharding.entity.ShardingTemplate;
import com.lego.sharding.service.IShardingTemplateService;
import com.lego.sharding.vo.ShardingTemplateCreateVO;
import com.lego.sharding.vo.ShardingTemplateModifyVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShardingTemplateService extends BusService<IShardingTemplateDao, ShardingTemplateAssembler> implements IShardingTemplateService {

    @Override
    public LegoPage<ShardingTemplateInfo> findPageBy(GenericSearchVO vo) {
        LegoPage<ShardingTemplate> templates = dao.findPageBy(buildCondition(vo));
        return assembler.create(templates);
    }

    @Override
    public ShardingTemplateInfo findBy(String code) {
        ShardingTemplate template = dao.findByCode(code);
        return assembler.create(template);
    }

    @Override
    public String findJsonBy(String code) {
        ShardingTemplate template = dao.findByCode(code);
        return template.getJson();
    }

    @Override
    public List<ShardingTemplateInfo> findBy(GenericSearchVO vo) {
        List<ShardingTemplate> templates = dao.findBy(buildCondition(vo));
        return assembler.create(templates);
    }

    @Override
    public List<ShardingTemplateInfo> findBy(List<String> codes) {
        List<ShardingTemplate> templates = dao.findByCodes(codes);
        return assembler.create(templates);
    }

    @Override
    public List<TypeInfo> findSimpleTypeBy(String typeCode) {
        GenericConditionVO conditionVO = GenericConditionVO.create();
        conditionVO.addItem(GenericConditionItemVO.createEqual("enable", true));
        conditionVO.addItem(GenericConditionItemVO.createEqual(CustomFieldTypeEnum.SELECT, "type", typeCode));
        List<ShardingTemplate> templates = dao.findBy(conditionVO);
        return assembler.createTypeInfo(templates);
    }

    @Override
    public void update(String operatorCode, ShardingTemplateModifyVO vo) {
        new ModifyShardingTemplateAction(operatorCode, vo).run();
    }

    @Override
    public void add(String operatorCode, ShardingTemplateCreateVO vo) {
        new AddShardingTemplateAction(operatorCode, vo).run();
    }

    @Override
    public void delete(String operatorCode, List<String> codes) {
        for (String code : codes) {
            new DeleteShardingTemplateAction(operatorCode, code).run();
        }
    }
}
