package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.EntityUtil;
import com.lego.system.entity.simpletype.SysDictionaryType;
import com.lego.system.service.ISysSimpleTypeService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SysSimpleTypeService extends BaseService implements ISysSimpleTypeService {

    @Override
    public List<TypeInfo> findDictionaryType() {
        List<SysDictionaryType> types = commonDao.findAll(SysDictionaryType.class);
        return EntityUtil.toSimpleTypeInfo(types);
    }
}
