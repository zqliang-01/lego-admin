package com.lego.sharding.action;

import com.lego.core.action.DeleteAction;
import com.lego.sharding.dao.IShardingAlgorithmDao;
import com.lego.sharding.entity.ShardingAlgorithm;
import com.lego.sharding.vo.ShardingPermissionCode;

public class DeleteShardingAlgorithmAction extends DeleteAction<ShardingAlgorithm, IShardingAlgorithmDao> {

    public DeleteShardingAlgorithmAction(String operatorCode, String code) {
        super(ShardingPermissionCode.shardingAlgorithm, operatorCode, code);
    }
}
