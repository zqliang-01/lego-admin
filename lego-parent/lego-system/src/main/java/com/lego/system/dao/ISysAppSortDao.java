package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysAppSort;

public interface ISysAppSortDao extends IGenericDao<SysAppSort> {

	List<SysAppSort> findByEmployee(String employeeCode);
}
