package com.lego.sharding.action;

import com.lego.core.action.DeleteAction;
import com.lego.sharding.dao.IShardingTableDao;
import com.lego.sharding.entity.ShardingTable;

public class DeleteShardingTableAction extends DeleteAction<ShardingTable, IShardingTableDao> {

    public DeleteShardingTableAction(String operatorCode, String code) {
        super("manage:sharding:table", operatorCode, code);
    }
}
