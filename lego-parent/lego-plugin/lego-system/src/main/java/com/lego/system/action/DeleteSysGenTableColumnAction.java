package com.lego.system.action;

import com.lego.core.action.DeleteAction;
import com.lego.core.exception.BusinessException;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;

public class DeleteSysGenTableColumnAction extends DeleteAction<SysGenTableColumn, ISysGenTableColumnDao> {

	public DeleteSysGenTableColumnAction(String operatorCode, String entityCode) {
		super(SysPermissionCode.manageGenTable, operatorCode, entityCode);
	}
}
