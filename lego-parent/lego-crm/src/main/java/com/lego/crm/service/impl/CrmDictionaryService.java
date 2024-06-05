package com.lego.crm.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.DictionaryInfo;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.EntityUtil;
import com.lego.core.vo.DictionaryTypeVO;
import com.lego.core.vo.DictionaryVO;
import com.lego.crm.action.AddCrmDictionaryAction;
import com.lego.crm.action.AddCrmDictionaryTypeAction;
import com.lego.crm.action.ModifyCrmDictionaryAction;
import com.lego.crm.action.ModifyCrmDictionaryTypeAction;
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
    public List<TypeInfo> findSimpleByType(String typeCode) {
        List<CrmDictionary> dictionaries = dictionaryDao.findValidByType(typeCode);
        return EntityUtil.toTypeInfo(dictionaries);
    }

    @Override
    public List<DictionaryInfo> findByType(String typeCode) {
        List<CrmDictionary> dictionaries = dictionaryDao.findByType(typeCode);
        return EntityUtil.toDictInfo(dictionaries);
    }

    @Override
    public void addType(String operator, DictionaryTypeVO vo) {
        new AddCrmDictionaryTypeAction(operator, vo).run();
    }

    @Override
    public void updateType(String operator, DictionaryTypeVO vo) {
        new ModifyCrmDictionaryTypeAction(operator, vo).run();
    }

    @Override
    public void add(String operator, DictionaryVO vo) {
        new AddCrmDictionaryAction(operator, vo).run();
    }

    @Override
    public void update(String operator, DictionaryVO vo) {
        new ModifyCrmDictionaryAction(operator, vo).run();
    }

}
