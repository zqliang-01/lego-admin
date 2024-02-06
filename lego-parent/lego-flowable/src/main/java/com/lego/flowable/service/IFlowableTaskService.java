package com.lego.flowable.service;

import com.lego.core.dto.LegoPage;
import com.lego.flowable.dto.FlowableProcessNodeInfo;
import com.lego.flowable.dto.FlowableTaskFormDetailInfo;
import com.lego.flowable.dto.FlowableTaskInfo;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.flowable.vo.FlowableTaskSearchVO;

public interface IFlowableTaskService {

    LegoPage<FlowableTaskInfo> findUndoBy(String employeeCode, FlowableTaskSearchVO vo);

    LegoPage<FlowableTaskInfo> findCompletedBy(String employeeCode, FlowableTaskSearchVO vo);

    void complete(String employeeCode, FlowableTaskCompleteVO vo);

    String getBpmnXml(String instanceId);

    FlowableProcessNodeInfo findProcessNodeBy(String instanceId);

    FlowableTaskFormDetailInfo findCodeVariable(String taskId);
}
