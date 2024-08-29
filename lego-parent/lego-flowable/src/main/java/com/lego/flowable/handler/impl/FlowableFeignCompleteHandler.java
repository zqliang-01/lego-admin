package com.lego.flowable.handler.impl;

import com.lego.core.common.ServiceStartType;
import com.lego.core.exception.CoreException;
import com.lego.core.feign.api.ITaskCompletedAPI;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.flowable.handler.IFlowableCompleteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "lego.start-type", havingValue = ServiceStartType.microservice)
public class FlowableFeignCompleteHandler implements IFlowableCompleteHandler {

    @Autowired
    private List<ITaskCompletedAPI> completedClients;

    @Override
    public String doTaskCompleted(String appCode, TaskCompletedVO vo) {
        return getClientBy(appCode).taskComplete(vo).getData();
    }

    @Override
    public void doTaskRejected(String appCode, String tableCode, String code) {
        getClientBy(appCode).taskReject(tableCode, code);
    }

    @Override
    public void doProcessCompleted(String appCode, String tableCode, String code) {
        getClientBy(appCode).processComplete(tableCode, code);
    }

    private ITaskCompletedAPI getClientBy(String appCode) {
        for (ITaskCompletedAPI client : completedClients) {
            if (client.accept(appCode)) {
                return client;
            }
        }
        throw new CoreException("未获取到[{0}]已注册的任务完工处理器，任务完工失败！", appCode);
    }
}
