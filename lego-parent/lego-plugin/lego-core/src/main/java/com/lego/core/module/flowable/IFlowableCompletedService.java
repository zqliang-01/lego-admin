package com.lego.core.module.flowable;

import com.lego.core.feign.vo.TaskCompletedVO;

public interface IFlowableCompletedService {

    String taskCompleted(TaskCompletedVO vo);

    void taskRejected(String tableCode, String code);

    void processCompleted(String tableCode, String code);
}
