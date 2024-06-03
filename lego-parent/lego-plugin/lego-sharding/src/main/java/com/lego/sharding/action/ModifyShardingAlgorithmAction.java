package com.lego.sharding.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingAlgorithmDao;
import com.lego.sharding.entity.ShardingAlgorithm;
import com.lego.sharding.entity.ShardingConfig;
import com.lego.sharding.entity.ShardingTemplate;
import com.lego.sharding.vo.ShardingAlgorithmModifyVO;
import com.lego.sharding.vo.ShardingPermissionCode;

public class ModifyShardingAlgorithmAction extends ModifyAction<ShardingAlgorithm, IShardingAlgorithmDao> {

    private ShardingAlgorithmModifyVO vo;

    public ModifyShardingAlgorithmAction(String operatorCode, ShardingAlgorithmModifyVO vo) {
        super(ShardingPermissionCode.shardingAlgorithm, operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，分片算法修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，分片算法修改失败！");
    }

    @Override
    protected void doModify(ShardingAlgorithm entity) {
        entity.setName(vo.getName());
        entity.setEnable(vo.isEnable());
        entity.setDescription(vo.getDescription());
        entity.setTemplate(findByCode(ShardingTemplate.class, vo.getTemplate()));
        entity.setConfig(findByCode(ShardingConfig.class, vo.getConfig()));
    }

}
