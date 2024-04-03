package com.lego.system.service;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.GenericSearchVO;
import com.lego.system.dto.SysPrintTemplateInfo;
import com.lego.system.vo.SysPrintTemplateCreateVO;
import com.lego.system.vo.SysPrintTemplateModifyVO;

import java.util.List;
import java.util.Map;

public interface ISysPrintTemplateService {

    LegoPage<SysPrintTemplateInfo> findPageBy(GenericSearchVO vo);

    List<TypeInfo> findSimpleBy(String formCode);

    SysPrintTemplateInfo findBy(String code);

    void update(String operatorCode, SysPrintTemplateModifyVO vo);

    void design(String operatorCode, SysPrintTemplateModifyVO vo);

    void add(String operatorCode, SysPrintTemplateCreateVO vo);

    void delete(String operatorCode, String code);

    String print(String code, Map<String, String> params);

    String preview(String content, String fileType);
}
