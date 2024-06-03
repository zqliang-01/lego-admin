package com.lego.sharding.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingTemplateDao;
import com.lego.sharding.entity.ShardingTemplate;
import com.lego.sharding.entity.ShardingTemplateType;
import com.lego.sharding.vo.ShardingPermissionCode;
import com.lego.sharding.vo.ShardingTemplateCreateVO;

public class AddShardingTemplateAction extends AddAction<ShardingTemplate, IShardingTemplateDao> {

    private ShardingTemplateCreateVO vo;

    public AddShardingTemplateAction(String operatorCode, ShardingTemplateCreateVO vo) {
        super(ShardingPermissionCode.shardingTemplate, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，分片模板新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，分片模板新增失败！");
    }

    @Override
    protected ShardingTemplate createTargetEntity() {
        ShardingTemplate entity = new ShardingTemplate(vo.getCode(), vo.getName());
        entity.setEnable(vo.isEnable());
        entity.setDescription(vo.getDescription());
        entity.setJson(vo.getJson());
        entity.setType(findByCode(ShardingTemplateType.class, vo.getType()));
        return entity;
    }

}
