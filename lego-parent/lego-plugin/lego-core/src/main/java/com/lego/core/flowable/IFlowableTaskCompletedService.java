package com.lego.core.flowable;

import com.lego.core.feign.vo.TaskCompletedVO;

public interface IFlowableTaskCompletedService {

    void completed(TaskCompletedVO vo);

    void processCompleted(String tableCode, String code);
}
