package com.lego.flowable.handler.impl;

import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.module.flowable.IFlowableCompletedService;
import com.lego.flowable.handler.IFlowableCompleteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(FlowableFeignCompleteHandler.class)
public class FlowableLocalCompleteHandler implements IFlowableCompleteHandler {

    @Autowired
    private IFlowableCompletedService completedService;

    @Override
    public String doTaskCompleted(String appCode, TaskCompletedVO vo) {
        return completedService.taskCompleted(vo);
    }

    @Override
    public void doTaskRejected(String appCode, String tableCode, String code) {
        completedService.taskRejected(tableCode, code);
    }

    @Override
    public void doProcessCompleted(String appCode, String tableCode, String code) {
        completedService.processCompleted(tableCode, code);
    }
}
