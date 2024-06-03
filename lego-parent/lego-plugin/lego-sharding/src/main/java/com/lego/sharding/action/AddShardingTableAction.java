package com.lego.sharding.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingTableDao;
import com.lego.sharding.entity.ShardingAlgorithm;
import com.lego.sharding.entity.ShardingConfig;
import com.lego.sharding.entity.ShardingDataSource;
import com.lego.sharding.entity.ShardingTable;
import com.lego.sharding.entity.ShardingTemplate;
import com.lego.sharding.vo.ShardingPermissionCode;
import com.lego.sharding.vo.ShardingTableCreateVO;

public class AddShardingTableAction extends AddAction<ShardingTable, IShardingTableDao> {

    private ShardingTableCreateVO vo;

    public AddShardingTableAction(String operatorCode, ShardingTableCreateVO vo) {
        super(ShardingPermissionCode.shardingTable, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，分片表新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，分片表新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getLogicTableName()), "逻辑表名称不能为空，分片表新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getActualDataNodes()), "物理表表达式不能为空，分片表新增失败！");
    }

    @Override
    protected ShardingTable createTargetEntity() {
        ShardingTable entity = new ShardingTable(vo.getCode(), vo.getName());
        entity.setEnable(vo.isEnable());
        entity.setDescription(vo.getDescription());
        entity.setLogicTableName(vo.getLogicTableName());
        entity.setActualDataNodes(vo.getActualDataNodes());
        entity.setShardingColumn(vo.getShardingColumn());
        entity.setAlgorithm(findByUnsureCode(ShardingAlgorithm.class, vo.getAlgorithm()));
        entity.setDataSource(findByUnsureCode(ShardingDataSource.class, vo.getDataSource()));
        entity.setTemplate(findByCode(ShardingTemplate.class, vo.getTemplate()));
        entity.setConfig(findByCode(ShardingConfig.class, vo.getConfig()));
        return entity;
    }

}
