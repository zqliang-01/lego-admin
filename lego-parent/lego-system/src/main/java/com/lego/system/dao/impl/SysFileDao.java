package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysFileDao;
import com.lego.system.entity.SysFile;

public class SysFileDao extends GenericDao<SysFile> implements ISysFileDao {

	@Override
	public List<SysFile> findBy(String entityCode, String permissionCode) {
		QueryHandler<SysFile> query = createQueryHandler();
		query.condition("t.entityCode = :entityCode").param("entityCode", entityCode);
		query.condition("t.permission.code = :permissionCode").param("permissionCode", permissionCode);
		return query.findList();
	}

}
