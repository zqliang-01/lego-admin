package com.lego.sharding.action;

import com.lego.core.action.DeleteAction;
import com.lego.sharding.dao.IShardingDataSourceDao;
import com.lego.sharding.entity.ShardingDataSource;
import com.lego.sharding.vo.ShardingPermissionCode;

public class DeleteShardingDataSourceAction extends DeleteAction<ShardingDataSource, IShardingDataSourceDao> {

    public DeleteShardingDataSourceAction(String operatorCode, String code) {
        super(ShardingPermissionCode.shardingDataSource, operatorCode, code);
    }
}
