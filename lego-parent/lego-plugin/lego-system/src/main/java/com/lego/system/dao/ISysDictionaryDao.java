package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysDictionary;

import java.util.List;

public interface ISysDictionaryDao extends IGenericDao<SysDictionary> {

    List<SysDictionary> findValidByType(String typeCode);

    List<SysDictionary> findByType(String typeCode);
}
