package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysCustomForm;

public interface ISysCustomFieldDao extends IGenericDao<SysCustomField> {

	List<SysCustomField> findBy(String formCode);

	List<SysCustomField> findValidBy(String formCode);

	List<SysCustomField> findByRelative(SysCustomForm relativeForm);
}
