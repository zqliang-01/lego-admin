package com.lego.crm.controller.remote;

import com.lego.core.common.ServiceStartType;
import com.lego.core.feign.api.ICrmAPI;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.flowable.IFlowableCompletedService;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-end/crm-flowable")
@ConditionalOnProperty(name = "lego.start-type", havingValue = ServiceStartType.microservice)
public class CrmFlowableController extends BaseController implements ICrmAPI {

    @Autowired
    private IFlowableCompletedService taskCompletedService;

    @Override
    @PostMapping("/task-complete")
    public JsonResponse<String> taskComplete(@RequestBody TaskCompletedVO vo) {
        return JsonResponse.success(taskCompletedService.taskCompleted(vo));
    }

    @Override
    @PostMapping("/task-reject")
    public JsonResponse<Object> taskReject(String tableCode, String code) {
        taskCompletedService.taskRejected(tableCode, code);
        return JsonResponse.success();
    }

    @Override
    @PostMapping("/process-complete")
    public JsonResponse<Object> processComplete(String tableCode, String code) {
        taskCompletedService.processCompleted(tableCode, code);
        return JsonResponse.success();
    }
}
