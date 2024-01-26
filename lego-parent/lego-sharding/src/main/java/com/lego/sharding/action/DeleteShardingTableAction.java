package com.lego.sharding.action;

import com.lego.core.action.DeleteAction;
import com.lego.sharding.dao.IShardingTableDao;
import com.lego.sharding.entity.ShardingTable;
import com.lego.sharding.vo.ShardingPermissionCode;

public class DeleteShardingTableAction extends DeleteAction<ShardingTable, IShardingTableDao> {

    public DeleteShardingTableAction(String operatorCode, String code) {
        super(ShardingPermissionCode.shardingTable, operatorCode, code);
    }
}
