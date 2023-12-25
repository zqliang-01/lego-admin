package com.lego.crm.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.crm.entity.CrmDictionary;

public interface ICrmDictionaryDao extends IGenericDao<CrmDictionary> {

	List<CrmDictionary> findBytype(String typeCode);
}
