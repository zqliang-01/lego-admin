package com.lego.flowable.service;

import com.lego.core.dto.LegoPage;
import com.lego.flowable.dto.FlowableDefinitionInfo;
import com.lego.flowable.vo.FlowableDefinitionSearchVO;
import org.flowable.common.engine.impl.db.SuspensionState;

import java.util.Map;

public interface IFlowableDefinitionService {

    LegoPage<FlowableDefinitionInfo> findBy(FlowableDefinitionSearchVO vo);

    LegoPage<FlowableDefinitionInfo> findHistBy(FlowableDefinitionSearchVO vo);

    String findBpmnXmlById(String id);

    void delete(String id);

    void updateStatus(String id, SuspensionState status);

    String start(String operatorCode, String definitionId, Map<String, Object> variables);
}
