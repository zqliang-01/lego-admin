package com.lego.core.data.hibernate;

import com.lego.core.dto.DictionaryInfo;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.DictionaryTypeVO;
import com.lego.core.vo.DictionaryVO;

import java.util.List;

public interface IDictionaryService {

    List<TypeInfo> findSimpleByType(String typeCode);

    List<DictionaryInfo> findByType(String typeCode);

    void addType(String operator, DictionaryTypeVO vo);

    void updateType(String operator, DictionaryTypeVO vo);

    void add(String operator, DictionaryVO vo);

    void update(String operator, DictionaryVO vo);
}
