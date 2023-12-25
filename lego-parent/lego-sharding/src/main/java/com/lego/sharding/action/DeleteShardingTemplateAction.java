package com.lego.sharding.action;

import com.lego.core.action.DeleteAction;
import com.lego.sharding.dao.IShardingTemplateDao;
import com.lego.sharding.entity.ShardingTemplate;

public class DeleteShardingTemplateAction extends DeleteAction<ShardingTemplate, IShardingTemplateDao> {

    public DeleteShardingTemplateAction(String operatorCode, String code) {
        super("manage:sharding:template", operatorCode, code);
    }
}
