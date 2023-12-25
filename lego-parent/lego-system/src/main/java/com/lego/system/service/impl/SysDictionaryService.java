package com.lego.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.TypeInfo;
import com.lego.system.dao.ISysDictionaryDao;
import com.lego.system.entity.SysDictionary;
import com.lego.system.service.ISysDictionaryService;

@Service
public class SysDictionaryService extends BaseService implements ISysDictionaryService {

	@Autowired
	private ISysDictionaryDao dictionaryDao;

	@Override
	public List<TypeInfo> findByType(String typeCode) {
		List<SysDictionary> dictionaries = dictionaryDao.findBytype(typeCode);
		return typeInfoAssembler.create(dictionaries);
	}
}
