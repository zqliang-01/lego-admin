package com.lego.sharding.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingTableDao;
import com.lego.sharding.entity.ShardingTable;
import com.lego.sharding.vo.ShardingTableModifyVO;
import com.lego.sharding.entity.ShardingTemplate;
import com.lego.sharding.entity.ShardingConfig;
import com.lego.sharding.entity.ShardingAlgorithm;

public class ModifyShardingTableAction extends ModifyAction<ShardingTable, IShardingTableDao> {

    private ShardingTableModifyVO vo;

    public ModifyShardingTableAction(String operatorCode, ShardingTableModifyVO vo) {
        super("manage:sharding:table", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，分片表修改失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，分片表修改失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getLogicTableName()), "逻辑表名称不能为空，分片表修改失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getActualDataNodes()), "物理表表达式不能为空，分片表修改失败！");
    }

    @Override
    protected void doModify(ShardingTable entity) {
        entity.setName(vo.getName());
        entity.setEnable(vo.isEnable());
        entity.setDescription(vo.getDescription());
        entity.setLogicTableName(vo.getLogicTableName());
        entity.setActualDataNodes(vo.getActualDataNodes());
        entity.setShardingColumn(vo.getShardingColumn());
        entity.setAlgorithm(findByUnsureCode(ShardingAlgorithm.class, vo.getAlgorithm()));
        entity.setTemplate(findByCode(ShardingTemplate.class, vo.getTemplate()));
        entity.setConfig(findByCode(ShardingConfig.class, vo.getConfig()));
    }

}
