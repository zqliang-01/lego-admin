package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysPrintLog;

import java.util.List;

public interface ISysPrintLogDao extends IGenericDao<SysPrintLog> {

    List<SysPrintLog> findBy(String permissionCode, String entityCode);
}
