package com.lego.flowable.controller;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.flowable.dto.FlowableInstanceInfo;
import com.lego.flowable.dto.FlowableProcessNodeInfo;
import com.lego.flowable.dto.IFlowableStartFormDetailInfo;
import com.lego.flowable.service.IFlowableInstanceService;
import com.lego.flowable.vo.FlowableInstanceSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/back-end/flowable-instance")
public class FlowableInstanceController extends BaseController {

    @Autowired
    private IFlowableInstanceService instanceService;

    @PostMapping("/list")
    public JsonResponse<LegoPage<FlowableInstanceInfo>> list(@RequestBody FlowableInstanceSearchVO vo) {
        return JsonResponse.success(instanceService.findBy(getLoginCode(), vo));
    }

    @GetMapping("/list-process-node/{id}")
    public JsonResponse<FlowableProcessNodeInfo> listProcessNode(@PathVariable String id) {
        FlowableProcessNodeInfo nodeInfo = instanceService.findProcessNodeBy(id);
        return JsonResponse.success(nodeInfo);
    }

    @PostMapping("/stop/{id}")
    public JsonResponse<Object> stop(@PathVariable String id) {
        instanceService.stop(getLoginCode(), id);
        return JsonResponse.success();
    }

    @GetMapping("/get-start-form/{id}")
    public JsonResponse<IFlowableStartFormDetailInfo> getStartForm(@PathVariable String id) {
        return JsonResponse.success(instanceService.findStartForm(id));
    }

    @GetMapping("/download-image/{id}")
    public void downloadImage(HttpServletResponse response, @PathVariable String id) {
        instanceService.downloadImage(response, id);
    }

}
