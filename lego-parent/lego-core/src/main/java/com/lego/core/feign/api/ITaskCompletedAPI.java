package com.lego.core.feign.api;

import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.vo.JsonResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 涉及跨服务事务受理，后续需加上Seata分布式事务
 */
public interface ITaskCompletedAPI {

    JsonResponse<Object> taskComplete(@RequestBody TaskCompletedVO vo);

    JsonResponse<Object> processComplete(@RequestParam String tableCode, @RequestParam String code);

    boolean accept(String appCode);
}
