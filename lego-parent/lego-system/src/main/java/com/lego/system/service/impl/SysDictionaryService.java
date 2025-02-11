package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.util.EntityUtil;
import com.lego.system.action.AddSysDictionaryAction;
import com.lego.system.action.AddSysDictionaryTypeAction;
import com.lego.system.action.ModifySysDictionaryAction;
import com.lego.system.action.ModifySysDictionaryTypeAction;
import com.lego.system.assembler.SysDictionaryAssembler;
import com.lego.system.dao.ISysDictionaryDao;
import com.lego.system.dto.SysDictionaryInfo;
import com.lego.system.entity.SysDictionary;
import com.lego.system.service.ISysDictionaryService;
import com.lego.system.vo.SysDictionaryTypeVO;
import com.lego.system.vo.SysDictionaryVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictionaryService extends BusService<ISysDictionaryDao, SysDictionaryAssembler> implements ISysDictionaryService {

    @Override
    public List<TypeInfo> findSimpleByType(String typeCode) {
        List<SysDictionary> dictionaries = dao.findValidByType(typeCode);
        return EntityUtil.toTypeInfo(dictionaries);
    }

    @Override
    public List<SysDictionaryInfo> findByType(String typeCode) {
        List<SysDictionary> dictionaries = dao.findByType(typeCode);
        return assembler.create(dictionaries);
    }

    @Override
    public void addType(String operator, SysDictionaryTypeVO vo) {
        new AddSysDictionaryTypeAction(operator, vo).run();
    }

    @Override
    public void updateType(String operator, SysDictionaryTypeVO vo) {
        new ModifySysDictionaryTypeAction(operator, vo).run();
    }

    @Override
    public void add(String operator, SysDictionaryVO vo) {
        new AddSysDictionaryAction(operator, vo).run();
    }

    @Override
    public void update(String operator, SysDictionaryVO vo) {
        new ModifySysDictionaryAction(operator, vo).run();
    }

}
