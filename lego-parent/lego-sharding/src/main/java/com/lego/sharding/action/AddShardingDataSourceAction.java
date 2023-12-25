package com.lego.sharding.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingDataSourceDao;
import com.lego.sharding.entity.ShardingDataSource;
import com.lego.sharding.vo.ShardingDataSourceCreateVO;
import com.lego.sharding.entity.ShardingTemplate;

public class AddShardingDataSourceAction extends AddAction<ShardingDataSource, IShardingDataSourceDao> {

    private ShardingDataSourceCreateVO vo;

    public AddShardingDataSourceAction(String operatorCode, ShardingDataSourceCreateVO vo) {
        super("manage:sharding:dataSource", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，分片数据源新增失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，分片数据源新增失败！");
    }

    @Override
    protected ShardingDataSource createTargetEntity() {
        ShardingDataSource entity = new ShardingDataSource(vo.getCode(), vo.getName());
        entity.setEnable(vo.isEnable());
        entity.setDescription(vo.getDescription());
        entity.setTemplate(findByCode(ShardingTemplate.class, vo.getTemplate()));
        return entity;
    }

}
