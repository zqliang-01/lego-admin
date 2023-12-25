package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysCustomForm;

public class SysCustomFieldDao extends GenericDao<SysCustomField> implements ISysCustomFieldDao {

	@Override
	public List<SysCustomField> findBy(String formCode) {
		QueryHandler<SysCustomField> handler = createQueryHandler();
		handler.condition("t.form.code = :formCode").param("formCode", formCode);
		return handler.findList();
	}

	@Override
	public List<SysCustomField> findValidBy(String formCode) {
		QueryHandler<SysCustomField> handler = createQueryHandler();
		handler.condition("t.form.code = :formCode").param("formCode", formCode);
		handler.condition("t.hidden = :hidden").param("hidden", false);
		return handler.findList();
	}

	@Override
	public List<SysCustomField> findByRelative(SysCustomForm relativeForm) {
		QueryHandler<SysCustomField> handler = createQueryHandler();
		handler.condition("t.relativeForm = :relativeForm").param("relativeForm", relativeForm);
		return handler.findList();
	}

}
