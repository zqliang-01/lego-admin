package com.lego.system.service;

import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysDictionaryInfo;
import com.lego.system.vo.SysDictionaryTypeVO;
import com.lego.system.vo.SysDictionaryVO;

import java.util.List;

public interface ISysDictionaryService {

    List<TypeInfo> findSimpleByType(String typeCode);

    List<SysDictionaryInfo> findByType(String typeCode);

    void addType(String operator, SysDictionaryTypeVO vo);

    void updateType(String operator, SysDictionaryTypeVO vo);

    void add(String operator, SysDictionaryVO vo);

    void update(String operator, SysDictionaryVO vo);
}
