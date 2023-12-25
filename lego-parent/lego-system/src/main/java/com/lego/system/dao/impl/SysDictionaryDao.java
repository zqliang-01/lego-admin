package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysDictionaryDao;
import com.lego.system.entity.SysDictionary;

public class SysDictionaryDao extends GenericDao<SysDictionary> implements ISysDictionaryDao {

	@Override
	public List<SysDictionary> findBytype(String typeCode) {
		QueryHandler<SysDictionary> query = createQueryHandler();
		query.condition("t.type.code = :typeCode").param("typeCode", typeCode);
		return query.findList();
	}

}
