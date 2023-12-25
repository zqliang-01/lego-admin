package com.lego.crm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.TypeInfo;
import com.lego.crm.entity.simpletype.CrmDictionaryType;
import com.lego.crm.service.ICrmSimpleTypeService;

@Service
public class CrmSimpleTypeService extends BaseService implements ICrmSimpleTypeService {

	@Override
	public List<TypeInfo> findDictionaryType() {
		List<CrmDictionaryType> types = commonDao.findAll(CrmDictionaryType.class);
		return typeInfoAssembler.create(types);
	}

}
