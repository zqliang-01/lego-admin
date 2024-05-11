package com.lego.core.feign.client;

import com.lego.core.common.AppCode;
import com.lego.core.feign.api.ISystemAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = AppCode.system, contextId = "systemRemote")
public interface ISystemClient extends ISystemAPI {

}
