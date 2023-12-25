package com.lego.sharding.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingAlgorithmDao;
import com.lego.sharding.entity.ShardingAlgorithm;
import com.lego.sharding.vo.ShardingAlgorithmCreateVO;
import com.lego.sharding.entity.ShardingTemplate;
import com.lego.sharding.entity.ShardingConfig;

public class AddShardingAlgorithmAction extends AddAction<ShardingAlgorithm, IShardingAlgorithmDao> {

    private ShardingAlgorithmCreateVO vo;

    public AddShardingAlgorithmAction(String operatorCode, ShardingAlgorithmCreateVO vo) {
        super("manage:sharding:algorithm", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，分片算法新增失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，分片算法新增失败！");
    }

    @Override
    protected ShardingAlgorithm createTargetEntity() {
        ShardingAlgorithm entity = new ShardingAlgorithm(vo.getCode(), vo.getName());
        entity.setEnable(vo.isEnable());
        entity.setDescription(vo.getDescription());
        entity.setTemplate(findByCode(ShardingTemplate.class, vo.getTemplate()));
        entity.setConfig(findByCode(ShardingConfig.class, vo.getConfig()));
        return entity;
    }

}
