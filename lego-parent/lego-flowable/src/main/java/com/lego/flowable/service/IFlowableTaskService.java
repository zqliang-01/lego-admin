package com.lego.flowable.service;

import com.lego.core.dto.LegoPage;
import com.lego.flowable.dto.FlowableTaskFormDetailInfo;
import com.lego.flowable.dto.FlowableTaskInfo;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.flowable.vo.FlowableTaskSearchVO;

import java.util.List;

public interface IFlowableTaskService {

    LegoPage<FlowableTaskInfo> findUndoBy(String employeeCode, FlowableTaskSearchVO vo);

    LegoPage<FlowableTaskInfo> findCompletedBy(String employeeCode, FlowableTaskSearchVO vo);

    void complete(String employeeCode, FlowableTaskCompleteVO vo);

    void reject(String employeeCode, FlowableTaskCompleteVO vo);

    FlowableTaskFormDetailInfo findCodeVariableBy(String taskId);

    List<FlowableTaskInfo> findBy(String instanceId, String key);
}
