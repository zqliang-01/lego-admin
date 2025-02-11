package com.lego.system.action;

import com.lego.core.action.DeleteAction;
import com.lego.system.dao.ISysCodeGeneratorDao;
import com.lego.system.entity.SysCodeGenerator;
import com.lego.system.vo.SysPermissionCode;

public class DeleteSysCodeGeneratorAction extends DeleteAction<SysCodeGenerator, ISysCodeGeneratorDao> {

	public DeleteSysCodeGeneratorAction(String operatorCode, String entityCode) {
		super(SysPermissionCode.manage, operatorCode, entityCode);
	}

}
