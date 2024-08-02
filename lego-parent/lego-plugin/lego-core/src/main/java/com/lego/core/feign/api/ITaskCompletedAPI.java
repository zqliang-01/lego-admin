package com.lego.core.feign.api;

import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.vo.JsonResponse;

/**
 * 涉及跨服务事务受理，后续需加上Seata分布式事务
 */
public interface ITaskCompletedAPI {

    JsonResponse<Object> taskComplete(TaskCompletedVO vo);

    JsonResponse<Object> processComplete(String tableCode, String code);

    boolean accept(String appCode);
}
