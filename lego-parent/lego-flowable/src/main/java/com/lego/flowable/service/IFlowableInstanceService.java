package com.lego.flowable.service;

import com.lego.core.dto.LegoPage;
import com.lego.flowable.dto.FlowableInstanceInfo;
import com.lego.flowable.dto.FlowableProcessNodeInfo;
import com.lego.flowable.vo.FlowableInstanceSearchVO;

public interface IFlowableInstanceService {

    LegoPage<FlowableInstanceInfo> findBy(String operatorCode, FlowableInstanceSearchVO vo);

    FlowableProcessNodeInfo findProcessNodeBy(String id);

    String getBpmnXml(String id);

    void stop(String operatorCode, String id);
}
