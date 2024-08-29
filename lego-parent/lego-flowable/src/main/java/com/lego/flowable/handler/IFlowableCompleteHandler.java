package com.lego.flowable.handler;

import com.lego.core.feign.vo.TaskCompletedVO;

public interface IFlowableCompleteHandler {

    String doTaskCompleted(String appCode, TaskCompletedVO vo);

    void doTaskRejected(String appCode, String tableCode, String code);

    void doProcessCompleted(String appCode, String tableCode, String code);
}
