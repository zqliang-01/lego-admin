package com.lego.sharding.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.enums.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.sharding.dao.IShardingConfigDao;
import com.lego.sharding.dao.IShardingPropertiesDao;
import com.lego.sharding.dao.IShardingTemplateDao;
import com.lego.sharding.entity.ShardingConfig;
import com.lego.sharding.entity.ShardingProperties;
import com.lego.sharding.entity.ShardingTemplate;
import com.lego.sharding.vo.ShardingPermissionCode;
import com.lego.sharding.vo.ShardingPropertiesCreateVO;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class ModifyShardingPropertiesAction extends MaintainAction {

    private ShardingPropertiesCreateVO vo;

    private IShardingTemplateDao templateDao = getDao(IShardingTemplateDao.class);
    private IShardingPropertiesDao propertiesDao = getDao(IShardingPropertiesDao.class);
    private IShardingConfigDao configDao = getDao(IShardingConfigDao.class);

    public ModifyShardingPropertiesAction(String operatorCode, ShardingPropertiesCreateVO vo) {
        super(ShardingPermissionCode.sharding, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(vo.getEntityId() != null, "属性归属对象编码不能为空！");
    }

    @Override
    protected void doRun() {
        delete();
        ShardingConfig config = configDao.findByUnsureCode(vo.getConfigCode());
        ShardingTemplate template = templateDao.findByCode(vo.getTemplateCode());
        List<ShardingProperties> properties = new ArrayList<>();
        for (Entry<String, String> entity : vo.getProperties().entrySet()) {
            ShardingProperties property = new ShardingProperties(entity.getKey(), entity.getValue());
            property.setEntityId(vo.getEntityId());
            property.setTemplate(template);
            property.setConfig(config);
            properties.add(property);
        }
        propertiesDao.saveAll(properties);
        this.description = MessageFormat.format("更新[{0}]分表属性{1}", vo.getEntityId(), EntityUtil.toString(properties));
    }

    private void delete() {
        List<ShardingProperties> properties = propertiesDao.findBy(vo.getEntityId());
        propertiesDao.deleteAllInBatch(properties);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return "分表属性";
    }
}
