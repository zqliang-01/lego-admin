package com.lego.system.service;

import com.lego.core.dto.LegoPage;
import com.lego.system.dto.SysFlowableDefinitionInfo;
import com.lego.system.dto.SysFlowableModelInfo;
import com.lego.system.vo.SysFlowableModelCreateVO;
import com.lego.system.vo.SysFlowableModelDesignVO;
import com.lego.system.vo.SysFlowableModelModifyVO;
import com.lego.system.vo.SysFlowableModelSearchVO;

import java.util.List;

public interface ISysFlowableModelService {

    LegoPage<SysFlowableModelInfo> findBy(SysFlowableModelSearchVO vo);

    List<SysFlowableDefinitionInfo> findDefinitionBy(String definitionId);

    String findBpmnXmlById(String key);

    void add(String operatorCode, SysFlowableModelCreateVO vo);

    void design(String operatorCode, SysFlowableModelDesignVO vo);

    void deploy(String operatorCode, String key);

    void delete(String operatorCode, String id);

    void update(String operatorCode, SysFlowableModelModifyVO vo);
}
