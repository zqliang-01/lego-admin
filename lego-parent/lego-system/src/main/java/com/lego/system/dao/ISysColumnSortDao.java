package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysColumnSort;

public interface ISysColumnSortDao extends IGenericDao<SysColumnSort> {

	List<SysColumnSort> findByPermission(String permissionCode, String employeeCode);
}
