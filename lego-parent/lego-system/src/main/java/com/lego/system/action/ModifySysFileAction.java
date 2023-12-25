package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.system.dao.ISysFileDao;
import com.lego.system.entity.SysFile;

public class ModifySysFileAction extends ModifyAction<SysFile, ISysFileDao> {

	private String name;

	public ModifySysFileAction(String permissionCode, String operatorCode, String entityCode, String name) {
		super(permissionCode, operatorCode, entityCode);
		this.name = name;
	}

	@Override
	protected void doModify(SysFile entity) {
		entity.setName(name);
	}

}
