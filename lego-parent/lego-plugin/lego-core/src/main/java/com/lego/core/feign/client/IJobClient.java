package com.lego.core.feign.client;

import com.lego.core.common.AppCode;
import com.lego.core.feign.api.IJobAPI;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 重写Xxl-job调度器接口，通过注册中心获取调度器地址
 */
@FeignClient(name = AppCode.job, contextId = "jobRemote")
public interface IJobClient extends IJobAPI {

}
