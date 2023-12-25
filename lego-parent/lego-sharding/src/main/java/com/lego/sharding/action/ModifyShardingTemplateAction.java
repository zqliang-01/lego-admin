package com.lego.sharding.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingTemplateDao;
import com.lego.sharding.entity.ShardingTemplate;
import com.lego.sharding.vo.ShardingTemplateModifyVO;
import com.lego.sharding.entity.ShardingTemplateType;

public class ModifyShardingTemplateAction extends ModifyAction<ShardingTemplate, IShardingTemplateDao> {

    private ShardingTemplateModifyVO vo;

    public ModifyShardingTemplateAction(String operatorCode, ShardingTemplateModifyVO vo) {
        super("manage:sharding:template", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，分片模板修改失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，分片模板修改失败！");
    }

    @Override
    protected void doModify(ShardingTemplate entity) {
        entity.setName(vo.getName());
        entity.setEnable(vo.isEnable());
        entity.setDescription(vo.getDescription());
        entity.setJson(vo.getJson());
        entity.setType(findByCode(ShardingTemplateType.class, vo.getType()));
    }

}
