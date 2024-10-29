package com.lego.flowable.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.flowable.dto.FlowableDefinitionInfo;
import com.lego.flowable.service.IFlowableDefinitionService;
import com.lego.flowable.vo.FlowableDefinitionSearchVO;
import com.lego.flowable.vo.FlowableTaskStartVO;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/back-end/flowable-definition")
public class FlowableDefinitionController extends BaseController {

    @Autowired
    private IFlowableDefinitionService definitionService;

    @PostMapping("/list")
    public JsonResponse<LegoPage<FlowableDefinitionInfo>> list(@RequestBody FlowableDefinitionSearchVO vo) {
        return JsonResponse.success(definitionService.findBy(vo));
    }

    @PostMapping("/list-history")
    @SaCheckPermission("manage_workflow_definition")
    public JsonResponse<LegoPage<FlowableDefinitionInfo>> listHistory(@RequestBody FlowableDefinitionSearchVO vo) {
        return JsonResponse.success(definitionService.findHistBy(vo));
    }

    @GetMapping("/getBpmnXml/{id}")
    public JsonResponse<String> getBpmnXml(@PathVariable String id) {
        return JsonResponse.success(definitionService.findBpmnXmlById(id));
    }

    @PostMapping("/delete/{id}")
    @SaCheckPermission("manage_workflow_definition_delete")
    public JsonResponse<Object> delete(@PathVariable String id) {
        definitionService.delete(id);
        return JsonResponse.success();
    }

    @PostMapping("/active/{id}")
    @SaCheckPermission("manage_workflow_definition_update")
    public JsonResponse<Object> active(@PathVariable String id) {
        definitionService.updateStatus(id, SuspensionState.ACTIVE);
        return JsonResponse.success();
    }

    @PostMapping("/suspend/{id}")
    @SaCheckPermission("manage_workflow_definition_update")
    public JsonResponse<Object> suspend(@PathVariable String id) {
        definitionService.updateStatus(id, SuspensionState.SUSPENDED);
        return JsonResponse.success();
    }

    @PostMapping("/start")
    public JsonResponse<Object> start(@RequestBody FlowableTaskStartVO vo) {
        definitionService.start(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @GetMapping("/get-form-key/{id}")
    public JsonResponse<Object> getFormKey(@PathVariable String id) {
        return JsonResponse.success(definitionService.findStartFormKey(id));
    }

    @GetMapping("/download-image/{id}")
    public void downloadImage(HttpServletResponse response, @PathVariable String id) {
        definitionService.downloadImage(response, id);
    }
}
