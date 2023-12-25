package com.lego.sharding.action;

import com.lego.core.action.DeleteAction;
import com.lego.sharding.dao.IShardingAlgorithmDao;
import com.lego.sharding.entity.ShardingAlgorithm;

public class DeleteShardingAlgorithmAction extends DeleteAction<ShardingAlgorithm, IShardingAlgorithmDao> {

    public DeleteShardingAlgorithmAction(String operatorCode, String code) {
        super("manage:sharding:algorithm", operatorCode, code);
    }
}
