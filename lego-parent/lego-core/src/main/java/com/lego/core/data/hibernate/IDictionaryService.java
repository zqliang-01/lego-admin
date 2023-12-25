package com.lego.core.data.hibernate;

import java.util.List;

import com.lego.core.dto.TypeInfo;

public interface IDictionaryService {

	List<TypeInfo> findByType(String typeCode);
}
