package com.lego.crm.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.EntityUtil;
import com.lego.crm.dao.ICrmDictionaryDao;
import com.lego.crm.entity.CrmDictionary;
import com.lego.crm.service.ICrmDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmDictionaryService extends BaseService implements ICrmDictionaryService {

    @Autowired
    private ICrmDictionaryDao dictionaryDao;

    @Override
    public List<TypeInfo> findByType(String typeCode) {
        List<CrmDictionary> dictionaries = dictionaryDao.findBytype(typeCode);
        return EntityUtil.toTypeInfo(dictionaries);
    }

}
