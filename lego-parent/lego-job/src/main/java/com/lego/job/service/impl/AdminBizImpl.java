package com.lego.job.service.impl;

import com.lego.core.job.biz.AdminBiz;
import com.lego.core.job.biz.model.HandleCallbackParam;
import com.lego.core.job.biz.model.RegistryParam;
import com.lego.core.job.biz.model.ReturnT;
import com.lego.job.core.thread.JobCompleteHelper;
import com.lego.job.core.thread.JobRegistryHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:54:20
 */
@Service
public class AdminBizImpl implements AdminBiz {


    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        return JobCompleteHelper.getInstance().callback(callbackParamList);
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        return JobRegistryHelper.getInstance().registry(registryParam);
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        return JobRegistryHelper.getInstance().registryRemove(registryParam);
    }

}
