package com.lego.sharding.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingDataSourceDao;
import com.lego.sharding.entity.ShardingDataSource;
import com.lego.sharding.vo.ShardingDataSourceModifyVO;
import com.lego.sharding.entity.ShardingTemplate;

public class ModifyShardingDataSourceAction extends ModifyAction<ShardingDataSource, IShardingDataSourceDao> {

    private ShardingDataSourceModifyVO vo;

    public ModifyShardingDataSourceAction(String operatorCode, ShardingDataSourceModifyVO vo) {
        super("manage:sharding:dataSource", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，分片数据源修改失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，分片数据源修改失败！");
    }

    @Override
    protected void doModify(ShardingDataSource entity) {
        entity.setName(vo.getName());
        entity.setEnable(vo.isEnable());
        entity.setDescription(vo.getDescription());
        entity.setTemplate(findByCode(ShardingTemplate.class, vo.getTemplate()));
    }

}
