package com.lego.core.feign.api;

import com.lego.core.job.biz.model.HandleCallbackParam;
import com.lego.core.job.biz.model.RegistryParam;
import com.lego.core.job.biz.model.ReturnT;
import com.lego.core.job.util.XxlJobRemotingUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface IJobAPI {

    @PostMapping("/back-end/job-api/callback")
    ReturnT<String> callback(@RequestBody List<HandleCallbackParam> callbackParamList, @RequestHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN) String accessToken);

    @PostMapping("/back-end/job-api/registry")
    ReturnT<String> registry(@RequestBody RegistryParam registryParam, @RequestHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN) String accessToken);

    @PostMapping("/back-end/job-api/registry")
    ReturnT<String> registryRemove(@RequestBody RegistryParam registryParam, @RequestHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN) String accessToken);
}
