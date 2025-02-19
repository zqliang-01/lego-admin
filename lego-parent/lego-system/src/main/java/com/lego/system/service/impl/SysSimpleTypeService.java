package com.lego.system.service.impl;

import com.lego.core.data.hibernate.ICommonDao;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.EntityUtil;
import com.lego.system.entity.simpletype.SysDictionaryType;
import com.lego.system.service.ISysSimpleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysSimpleTypeService implements ISysSimpleTypeService {

    @Autowired
    protected ICommonDao commonDao;

    @Override
    public List<TypeInfo> findDictionaryType() {
        List<SysDictionaryType> types = commonDao.findAll(SysDictionaryType.class);
        return EntityUtil.toSimpleTypeInfo(types);
    }
}
