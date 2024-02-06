package com.lego.flowable.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.flowable.dto.FlowableModelInfo;
import com.lego.flowable.service.IFlowableModelService;
import com.lego.flowable.vo.FlowableModelCreateVO;
import com.lego.flowable.vo.FlowableModelDesignVO;
import com.lego.flowable.vo.FlowableModelModifyVO;
import com.lego.flowable.vo.FlowableModelSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-end/flowable-model")
public class FlowableModelController extends BaseController {

    @Autowired
    private IFlowableModelService modelService;

    @PostMapping("/list")
    @SaCheckPermission("manage_workflow_model")
    public JsonResponse<LegoPage<FlowableModelInfo>> list(@RequestBody FlowableModelSearchVO vo) {
        return JsonResponse.success(modelService.findBy(vo));
    }

    @GetMapping("/list-history")
    @SaCheckPermission("manage_workflow_model")
    public JsonResponse<LegoPage<FlowableModelInfo>> listHistory(@RequestBody FlowableModelSearchVO vo) {
        return JsonResponse.success(modelService.findHistBy(vo));
    }

    @GetMapping("/getBpmnXml/{id}")
    public JsonResponse<String> getBpmnXml(@PathVariable String id) {
        return JsonResponse.success(modelService.findBpmnXmlById(id));
    }

    @PostMapping("/add")
    @SaCheckPermission("manage_workflow_model_add")
    public JsonResponse<Object> save(@RequestBody FlowableModelCreateVO vo) {
        modelService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("manage_workflow_model_update")
    public JsonResponse<Object> update(@RequestBody FlowableModelModifyVO vo) {
        modelService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/design")
    @SaCheckPermission("manage_workflow_model_update")
    public JsonResponse<Object> design(@RequestBody FlowableModelDesignVO vo) {
        modelService.design(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/deploy/{id}")
    @SaCheckPermission("manage_workflow_model_deploy")
    public JsonResponse<Object> deploy(@PathVariable String id) {
        modelService.deploy(getLoginCode(), id);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{id}")
    @SaCheckPermission("manage_workflow_model_delete")
    public JsonResponse<Object> delete(@PathVariable String id) {
        modelService.delete(getLoginCode(), id);
        return JsonResponse.success();
    }
}
