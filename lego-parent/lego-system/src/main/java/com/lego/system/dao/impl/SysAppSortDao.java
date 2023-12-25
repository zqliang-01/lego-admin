package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysAppSortDao;
import com.lego.system.entity.SysAppSort;

public class SysAppSortDao extends GenericDao<SysAppSort> implements ISysAppSortDao {

	@Override
	public List<SysAppSort> findByEmployee(String employeeCode) {
		QueryHandler<SysAppSort> query = createQueryHandler();
		query.condition("t.employee.code = :employeeCode").param("employeeCode", employeeCode);
		return query.findList();
	}

}
