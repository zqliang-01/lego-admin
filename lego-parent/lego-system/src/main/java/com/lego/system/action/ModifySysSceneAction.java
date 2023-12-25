package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.system.dao.ISysSceneDao;
import com.lego.system.entity.SysScene;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysSceneModifyVO;

public class ModifySysSceneAction extends ModifyAction<SysScene, ISysSceneDao> {

	private SysSceneModifyVO vo;

	public ModifySysSceneAction(String operatorCode, SysSceneModifyVO vo) {
		super(SysPermissionCode.manage, operatorCode, vo.getCode());
		this.vo = vo;
	}

	@Override
	protected void doModify(SysScene entity) {
		entity.setName(vo.getName());
		entity.setData(vo.getData());
		entity.setCurrent(vo.isCurrent());
		entity.setVisible(vo.isEnable());
	}

}
