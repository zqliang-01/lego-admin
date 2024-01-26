package com.lego.sharding.action;

import com.lego.core.action.DeleteAction;
import com.lego.sharding.dao.IShardingConfigDao;
import com.lego.sharding.entity.ShardingConfig;
import com.lego.sharding.vo.ShardingPermissionCode;

public class DeleteShardingConfigAction extends DeleteAction<ShardingConfig, IShardingConfigDao> {

    public DeleteShardingConfigAction(String operatorCode, String code) {
        super(ShardingPermissionCode.shardingConfig, operatorCode, code);
    }
}
