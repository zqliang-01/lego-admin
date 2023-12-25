package com.lego.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.TypeInfo;
import com.lego.system.entity.simpletype.SysDictionaryType;
import com.lego.system.service.ISysSimpleTypeService;

@Service
public class SysSimpleTypeService extends BaseService implements ISysSimpleTypeService {

	@Override
	public List<TypeInfo> findDictionaryType() {
		List<SysDictionaryType> types = commonDao.findAll(SysDictionaryType.class);
		return typeInfoAssembler.create(types);
	}

}
