package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysDictionary;

public interface ISysDictionaryDao extends IGenericDao<SysDictionary> {

	List<SysDictionary> findBytype(String typeCode);
}
