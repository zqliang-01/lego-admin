package com.lego.flowable.service;

import com.lego.core.dto.LegoPage;
import com.lego.flowable.dto.FlowableInstanceInfo;
import com.lego.flowable.dto.FlowableProcessNodeInfo;
import com.lego.flowable.dto.IFlowableStartFormDetailInfo;
import com.lego.flowable.vo.FlowableInstanceSearchVO;

import javax.servlet.http.HttpServletResponse;

public interface IFlowableInstanceService {

    LegoPage<FlowableInstanceInfo> findBy(String operatorCode, FlowableInstanceSearchVO vo);

    FlowableProcessNodeInfo findProcessNodeBy(String id);

    void stop(String operatorCode, String id);

    IFlowableStartFormDetailInfo findStartForm(String id);

    void downloadImage(HttpServletResponse response, String id);
}
