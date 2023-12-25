package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysSceneDao;
import com.lego.system.entity.SysScene;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysSceneCreateVO;

public class AddSysSceneAction extends AddAction<SysScene, ISysSceneDao> {

	private SysSceneCreateVO vo;

	private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);
	private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);

	public AddSysSceneAction(String operatorCode, SysSceneCreateVO vo) {
		super(SysPermissionCode.manage, operatorCode);
		this.vo = vo;
	}

	@Override
	protected SysScene createTargetEntity() {
		SysScene scene = new SysScene(vo.getName());
		scene.setForm(formDao.findByCode(vo.getFormCode()));
		scene.setEmployee(employeeDao.findByCode(operatorCode));
		scene.setData(vo.getData());
		scene.setCurrent(vo.isCurrent());
		return scene;
	}

}
