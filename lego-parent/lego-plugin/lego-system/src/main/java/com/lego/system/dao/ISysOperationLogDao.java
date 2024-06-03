package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysOperationLog;

public interface ISysOperationLogDao extends IGenericDao<SysOperationLog> {

	List<SysOperationLog> findBy(String loginCode, String entityCode, String permissionCode);

}
