package com.lego.core.feign.api;

import com.lego.core.common.AppCode;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.vo.JsonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ICrmAPI extends ITaskCompletedAPI {

    @PostMapping("/back-end/crm-flowable/task-complete")
    JsonResponse<Object> taskComplete(@RequestBody TaskCompletedVO vo);

    @PostMapping("/back-end/crm-flowable/process-complete")
    JsonResponse<Object> processComplete(@RequestParam String tableCode, @RequestParam String code);

    @Override
    default boolean accept(String appCode) {
        return AppCode.crm.equals(appCode);
    }
}
