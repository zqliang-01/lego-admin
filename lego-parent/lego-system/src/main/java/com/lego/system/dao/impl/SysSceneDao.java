package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysSceneDao;
import com.lego.system.entity.SysScene;

public class SysSceneDao extends GenericDao<SysScene> implements ISysSceneDao {

	@Override
	public List<SysScene> findByForm(String formCode) {
		QueryHandler<SysScene> handler = createQueryHandler();
		handler.condition("t.form.code = :formCode").param("formCode", formCode);
		return handler.findList();
	}

	@Override
	public List<SysScene> findBy(String formCode, boolean visible) {
		QueryHandler<SysScene> handler = createQueryHandler();
		handler.condition("t.form.code = :formCode").param("formCode", formCode);
		handler.condition("t.visible = :visible").param("visible", visible);
		return handler.findList();
	}

}
