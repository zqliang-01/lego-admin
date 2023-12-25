package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.entity.SysColumnSort;

public class SysColumnSortDao extends GenericDao<SysColumnSort> implements ISysColumnSortDao {

	@Override
	public List<SysColumnSort> findByPermission(String permissionCode, String employeeCode) {
		QueryHandler<SysColumnSort> query = createQueryHandler();
		query.condition("t.field.form.permission.code = :permissionCode").param("permissionCode", permissionCode);
		query.condition("t.employee.code = :employeeCode").param("employeeCode", employeeCode);
		query.condition("t.field.hidden = :hidden").param("hidden", false);
		query.order("t.sn");
		return query.findList();
	}

}
