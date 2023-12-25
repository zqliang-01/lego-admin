package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysOperationLogDao;
import com.lego.system.entity.SysOperationLog;

public class SysOperationLogDao extends GenericDao<SysOperationLog> implements ISysOperationLogDao {

	@Override
	public List<SysOperationLog> findBy(String loginCode, String entityCode, String permissionCode) {
		QueryHandler<SysOperationLog> query = createQueryHandler();
		query.condition("t.entityCode = :entityCode").param("entityCode", entityCode);
//		query.condition("t.operator.code = :operatorCode").param("operatorCode", loginCode);
		query.condition("t.permission.code = :permissionCode").param("permissionCode", permissionCode);
		query.order("t.createTime DESC");
		return query.findList();
	}

}
