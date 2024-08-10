package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysConfigDao;
import com.lego.system.entity.SysConfig;

public class SysConfigDao extends GenericDao<SysConfig> implements ISysConfigDao {

    @Override
    public String findValueBy(String code) {
        QueryHandler<String> query = createQueryHandler("SELECT t.value FROM sys_config t", String.class);
        query.condition("t.code = :code").param("code", code);
        return query.findSqlUnique();
    }
}
