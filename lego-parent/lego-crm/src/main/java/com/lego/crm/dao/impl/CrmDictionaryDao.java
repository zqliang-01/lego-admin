package com.lego.crm.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.crm.dao.ICrmDictionaryDao;
import com.lego.crm.entity.CrmDictionary;

import java.util.List;

public class CrmDictionaryDao extends GenericDao<CrmDictionary> implements ICrmDictionaryDao {

    @Override
    public List<CrmDictionary> findValidByType(String typeCode) {
        QueryHandler<CrmDictionary> query = createQueryHandler();
        query.condition("t.type.code = :typeCode").param("typeCode", typeCode);
        query.condition("t.enable = :enable").param("enable", true);
        query.order("t.serialNumber");
        return query.findList();
    }

    @Override
    public List<CrmDictionary> findByType(String typeCode) {
        QueryHandler<CrmDictionary> query = createQueryHandler();
        query.condition("t.type.code = :typeCode").param("typeCode", typeCode);
        query.order("t.serialNumber");
        return query.findList();
    }

}
