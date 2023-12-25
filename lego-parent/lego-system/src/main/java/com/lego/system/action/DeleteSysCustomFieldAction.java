package com.lego.system.action;

import com.lego.core.action.DeleteAction;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.entity.SysCustomField;
import com.lego.system.vo.SysPermissionCode;

public class DeleteSysCustomFieldAction extends DeleteAction<SysCustomField, ISysCustomFieldDao> {

	public DeleteSysCustomFieldAction(String operatorCode, String fieldCode) {
		super(SysPermissionCode.manageCustomForm, operatorCode, fieldCode);
	}

	@Override
	protected void createLog() { }
}
