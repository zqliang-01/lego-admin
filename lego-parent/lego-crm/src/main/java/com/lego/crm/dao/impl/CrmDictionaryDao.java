package com.lego.crm.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.crm.dao.ICrmDictionaryDao;
import com.lego.crm.entity.CrmDictionary;

public class CrmDictionaryDao extends GenericDao<CrmDictionary> implements ICrmDictionaryDao {

	@Override
	public List<CrmDictionary> findBytype(String typeCode) {
		QueryHandler<CrmDictionary> query = createQueryHandler();
		query.condition("t.type.code = :typeCode").param("typeCode", typeCode);
		return query.findList();
	}

}
