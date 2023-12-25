package com.lego.system.action;

import com.lego.core.action.DeleteAction;
import com.lego.core.web.LegoBeanFactory;
import com.lego.core.web.upload.FileHandler;
import com.lego.system.dao.ISysFileDao;
import com.lego.system.entity.SysFile;

public class DeleteSysFileAction extends DeleteAction<SysFile, ISysFileDao> {

	private FileHandler fileHandler = LegoBeanFactory.getBean(FileHandler.class);

	public DeleteSysFileAction(String permissionCode, String operatorCode, String code) {
		super(permissionCode, operatorCode, code);
	}

	@Override
	protected void preprocess() {
		fileHandler.delete(targetEntity.getPath());
	}

}
