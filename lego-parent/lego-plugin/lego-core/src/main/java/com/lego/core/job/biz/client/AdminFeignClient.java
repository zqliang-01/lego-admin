package com.lego.core.job.biz.client;

import com.lego.core.feign.client.IJobClient;
import com.lego.core.job.biz.AdminBiz;
import com.lego.core.job.biz.model.HandleCallbackParam;
import com.lego.core.job.biz.model.RegistryParam;
import com.lego.core.job.biz.model.ReturnT;
import com.lego.core.web.LegoBeanFactory;
import lombok.Getter;

import java.util.List;

public class AdminFeignClient implements AdminBiz {

    @Getter
    private String appName;
    private String accessToken;

    private IJobClient jobClient = LegoBeanFactory.getBean(IJobClient.class);

    public AdminFeignClient(String appName, String accessToken) {
        this.appName = appName;
        this.accessToken = accessToken;
    }

    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        return jobClient.callback(callbackParamList, accessToken);
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        return jobClient.registry(registryParam, accessToken);
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        return jobClient.registryRemove(registryParam, accessToken);
    }
}
