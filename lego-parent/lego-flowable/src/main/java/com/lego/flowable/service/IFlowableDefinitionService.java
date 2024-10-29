package com.lego.flowable.service;

import com.lego.core.dto.LegoPage;
import com.lego.flowable.dto.FlowableDefinitionInfo;
import com.lego.flowable.vo.FlowableDefinitionSearchVO;
import com.lego.flowable.vo.FlowableTaskStartVO;
import org.flowable.common.engine.impl.db.SuspensionState;

import javax.servlet.http.HttpServletResponse;

public interface IFlowableDefinitionService {

    LegoPage<FlowableDefinitionInfo> findBy(FlowableDefinitionSearchVO vo);

    LegoPage<FlowableDefinitionInfo> findHistBy(FlowableDefinitionSearchVO vo);

    String findBpmnXmlById(String id);

    void delete(String id);

    void updateStatus(String id, SuspensionState status);

    void start(String operatorCode, FlowableTaskStartVO vo);

    String findStartFormKey(String id);

    void downloadImage(HttpServletResponse response, String id);
}
