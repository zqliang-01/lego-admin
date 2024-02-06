package com.lego.flowable.service;

import com.lego.core.dto.LegoPage;
import com.lego.flowable.dto.FlowableModelInfo;
import com.lego.flowable.vo.FlowableModelCreateVO;
import com.lego.flowable.vo.FlowableModelDesignVO;
import com.lego.flowable.vo.FlowableModelModifyVO;
import com.lego.flowable.vo.FlowableModelSearchVO;

public interface IFlowableModelService {

    LegoPage<FlowableModelInfo> findBy(FlowableModelSearchVO vo);

    LegoPage<FlowableModelInfo> findHistBy(FlowableModelSearchVO vo);

    String findBpmnXmlById(String id);

    void add(String operatorCode, FlowableModelCreateVO vo);

    void design(String operatorCode, FlowableModelDesignVO vo);

    void deploy(String operatorCode, String key);

    void delete(String operatorCode, String id);

    void update(String operatorCode, FlowableModelModifyVO vo);
}
