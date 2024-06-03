package com.lego.core.feign.client;

import com.lego.core.common.AppCode;
import com.lego.core.feign.api.ICrmAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = AppCode.crm, contextId = "crmRemote")
public interface ICrmClient extends ICrmAPI {

}
