package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysPrintLogDao;
import com.lego.system.entity.SysPrintLog;

import java.util.List;

public class SysPrintLogDao extends GenericDao<SysPrintLog> implements ISysPrintLogDao {

    @Override
    public List<SysPrintLog> findBy(String permissionCode, String entityCode) {
        QueryHandler<SysPrintLog> query = createQueryHandler();
        query.condition("t.permission.code = :permissionCode").param("permissionCode", permissionCode);
        query.condition("t.entityCode = :entityCode").param("entityCode", entityCode);
        return query.findList();
    }
}
