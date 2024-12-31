package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysDictionaryDao;
import com.lego.system.entity.SysDictionary;

import java.util.List;

public class SysDictionaryDao extends GenericDao<SysDictionary> implements ISysDictionaryDao {

    @Override
    public List<SysDictionary> findValidByType(String typeCode) {
        QueryHandler<SysDictionary> query = createQueryHandler();
        query.condition("t.type.code = :typeCode").param("typeCode", typeCode);
        query.condition("t.enable = :enable").param("enable", true);
        query.order("t.serialNumber");
        return query.findList();
    }

    @Override
    public List<SysDictionary> findByType(String typeCode) {
        QueryHandler<SysDictionary> query = createQueryHandler();
        query.condition("t.type.code = :typeCode").param("typeCode", typeCode);
        query.order("t.serialNumber");
        return query.findList();
    }

}
