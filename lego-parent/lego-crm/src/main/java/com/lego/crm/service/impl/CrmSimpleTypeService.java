package com.lego.crm.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.EntityUtil;
import com.lego.crm.entity.simpletype.CrmDictionaryType;
import com.lego.crm.service.ICrmSimpleTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmSimpleTypeService extends BaseService implements ICrmSimpleTypeService {

    @Override
    public List<TypeInfo> findDictionaryType() {
        List<CrmDictionaryType> types = commonDao.findAll(CrmDictionaryType.class);
        return EntityUtil.toSimpleTypeInfo(types);
    }

}
