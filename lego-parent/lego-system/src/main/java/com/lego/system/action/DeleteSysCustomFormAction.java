package com.lego.system.action;

import java.util.List;

import com.lego.core.action.DeleteAction;
import com.lego.core.exception.BusinessException;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.vo.SysPermissionCode;

public class DeleteSysCustomFormAction extends DeleteAction<SysCustomForm, ISysCustomFormDao> {

	private ISysCustomFieldDao fieldDao = getDao(ISysCustomFieldDao.class);

	public DeleteSysCustomFormAction(String operatorCode, String entityCode) {
		super(SysPermissionCode.manageCustomForm, operatorCode, entityCode);
	}

	@Override
	protected void preprocess() {
		List<SysCustomField> relativeForms = fieldDao.findByRelative(targetEntity);
		if (!relativeForms.isEmpty()) {
			throw new BusinessException("当前表单被[{0}]表单所关联，请先解除关联后再删除！", relativeForms.get(0).getForm().getName());
		}
	}

	@Override
	protected void doRun() {
		List<SysCustomField> fields = fieldDao.findBy(targetEntity.getCode());
		for (SysCustomField field : fields) {
			new DeleteSysCustomFieldAction(operatorCode, field.getCode()).run();
		}
		super.doRun();
	}
}
