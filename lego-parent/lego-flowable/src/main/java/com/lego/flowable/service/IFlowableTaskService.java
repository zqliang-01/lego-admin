package com.lego.flowable.service;

import com.lego.core.dto.LegoPage;
import com.lego.flowable.dto.FlowableTaskFormDetailInfo;
import com.lego.flowable.dto.FlowableTaskInfo;
import com.lego.flowable.vo.FLowbaleTaskClaimVO;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.flowable.vo.FlowableTaskDelegateVO;
import com.lego.flowable.vo.FlowableTaskRejectVO;
import com.lego.flowable.vo.FlowableTaskSearchVO;
import com.lego.flowable.vo.FlowableTaskTransferVO;

import java.util.List;

public interface IFlowableTaskService {

    LegoPage<FlowableTaskInfo> findUndoBy(String employeeCode, FlowableTaskSearchVO vo);

    LegoPage<FlowableTaskInfo> findCompletedBy(String employeeCode, FlowableTaskSearchVO vo);

    LegoPage<FlowableTaskInfo> findClaimdBy(String employeeCode, FlowableTaskSearchVO vo);

    FlowableTaskFormDetailInfo findCodeVariableBy(String taskId);

    List<FlowableTaskInfo> findBy(String instanceId, String key);

    void complete(String employeeCode, FlowableTaskCompleteVO vo);

    void save(String employeeCode, FlowableTaskCompleteVO vo);

    void reject(String employeeCode, FlowableTaskRejectVO vo);

    void delegate(String employeeCode, FlowableTaskDelegateVO vo);

    void claim(String loginCode, FLowbaleTaskClaimVO vo);

    void unClaim(String loginCode, FLowbaleTaskClaimVO vo);

    void transfer(String loginCode, FlowableTaskTransferVO vo);
}
