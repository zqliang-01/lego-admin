package com.lego.flowable.handler;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.common.ServiceStartType;
import com.lego.core.feign.api.ITaskCompletedAPI;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.flowable.IFlowableTaskCompletedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlowableTaskCompleteHandler {

    @Value("${lego.start-type:}")
    private String startType;

    @Autowired(required = false)
    private List<ITaskCompletedAPI> completedClients;

    @Autowired(required = false)
    private List<IFlowableTaskCompletedService> completedServices;

    public void doCompleted(String appCode, TaskCompletedVO vo) {
        if (ServiceStartType.microservice.equals(startType)) {
            for (ITaskCompletedAPI client : completedClients) {
                if (client.accept(appCode)) {
                    client.taskComplete(vo);
                }
            }
            return;
        }
        if (CollectionUtil.isEmpty(completedServices)) {
            return;
        }
        for (IFlowableTaskCompletedService completedService : completedServices) {
            if (completedService.accept(appCode)) {
                completedService.completed(vo);
                return;
            }
        }
    }

    public void doProcessCompleted(String appCode, String tableCode, String code) {
        if (ServiceStartType.microservice.equals(startType)) {
            for (ITaskCompletedAPI client : completedClients) {
                if (client.accept(appCode)) {
                    client.processComplete(tableCode, code);
                }
            }
            return;
        }
        if (CollectionUtil.isEmpty(completedServices)) {
            return;
        }
        for (IFlowableTaskCompletedService completedService : completedServices) {
            if (completedService.accept(appCode)) {
                completedService.processCompleted(tableCode, code);
                return;
            }
        }
    }
}
