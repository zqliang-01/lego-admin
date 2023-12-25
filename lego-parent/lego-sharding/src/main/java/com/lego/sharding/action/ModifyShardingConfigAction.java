package com.lego.sharding.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingConfigDao;
import com.lego.sharding.entity.ShardingConfig;
import com.lego.sharding.vo.ShardingConfigModifyVO;

public class ModifyShardingConfigAction extends ModifyAction<ShardingConfig, IShardingConfigDao> {

    private ShardingConfigModifyVO vo;

    public ModifyShardingConfigAction(String operatorCode, ShardingConfigModifyVO vo) {
        super("manage:sharding:config", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，分片规则修改失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，分片规则修改失败！");
    }

    @Override
    protected void doModify(ShardingConfig entity) {
        entity.setName(vo.getName());
        entity.setEnable(vo.isEnable());
        entity.setDescription(vo.getDescription());
    }

}
