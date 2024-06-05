package com.lego.crm.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.crm.entity.CrmDictionary;

import java.util.List;

public interface ICrmDictionaryDao extends IGenericDao<CrmDictionary> {

    List<CrmDictionary> findValidByType(String typeCode);

    List<CrmDictionary> findByType(String typeCode);
}
