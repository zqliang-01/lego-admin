package com.lego.sharding.action;

import java.util.List;
import java.util.Map.Entry;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dao.IShardingConfigDao;
import com.lego.sharding.dao.IShardingPropertiesDao;
import com.lego.sharding.dao.IShardingTemplateDao;
import com.lego.sharding.entity.ShardingConfig;
import com.lego.sharding.entity.ShardingProperties;
import com.lego.sharding.entity.ShardingTemplate;
import com.lego.sharding.vo.ShardingPermissionCode;
import com.lego.sharding.vo.ShardingPropertiesCreateVO;

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
		BusinessException.check(StringUtil.isNotBlank(vo.getEntityCode()), "属性归属对象编码不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getTemplateCode()), "属性归属模板编码不能为空！");
	}

	@Override
	protected void doRun() {
		delete();
		ShardingConfig config = configDao.findByUnsureCode(vo.getConfigCode());
		ShardingTemplate template = templateDao.findByCode(vo.getTemplateCode());
		for (Entry<String, String> entity : vo.getProperties().entrySet()) {
			ShardingProperties property = new ShardingProperties(entity.getKey(), entity.getValue());
			property.setEntityCode(vo.getEntityCode());
			property.setTemplate(template);
			property.setConfig(config);
			propertiesDao.save(property);
		}
	}

	private void delete() {
		List<ShardingProperties> properties = propertiesDao.findBy(vo.getEntityCode(), vo.getTemplateCode(), vo.getConfigCode());
		propertiesDao.deleteAllInBatch(properties);
	}

	@Override
	protected ActionType getActionType() {
		return ActionType.MODIFY;
	}

}
