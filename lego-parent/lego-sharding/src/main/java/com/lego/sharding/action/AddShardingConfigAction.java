package com.lego.sharding.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingConfigDao;
import com.lego.sharding.entity.ShardingConfig;
import com.lego.sharding.vo.ShardingConfigCreateVO;

public class AddShardingConfigAction extends AddAction<ShardingConfig, IShardingConfigDao> {

    private ShardingConfigCreateVO vo;

    public AddShardingConfigAction(String operatorCode, ShardingConfigCreateVO vo) {
        super("manage:sharding:config", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，分片规则新增失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，分片规则新增失败！");
    }

    @Override
    protected ShardingConfig createTargetEntity() {
        ShardingConfig entity = new ShardingConfig(vo.getCode(), vo.getName());
        entity.setEnable(vo.isEnable());
        entity.setDescription(vo.getDescription());
        return entity;
    }

}
