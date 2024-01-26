package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysFlowableDefinitionInfo;
import com.lego.system.dto.SysFlowableModelInfo;
import com.lego.system.service.ISysFlowableModelService;
import com.lego.system.vo.SysFlowableModelCreateVO;
import com.lego.system.vo.SysFlowableModelDesignVO;
import com.lego.system.vo.SysFlowableModelModifyVO;
import com.lego.system.vo.SysFlowableModelSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-flowable-model")
public class SysFlowableModelController extends BaseController {

    @Autowired
    private ISysFlowableModelService modelService;

    @GetMapping("/list")
    @SaCheckPermission("workflow_model_read")
    public JsonResponse<LegoPage<SysFlowableModelInfo>> list(SysFlowableModelSearchVO vo) {
        return JsonResponse.success(modelService.findBy(vo));
    }

    @GetMapping("/list-definition/{definitionId}")
    @SaCheckPermission("workflow_model_read")
    public JsonResponse<List<SysFlowableDefinitionInfo>> list(@PathVariable String definitionId) {
        return JsonResponse.success(modelService.findDefinitionBy(definitionId));
    }

    @GetMapping("/getBpmnXml/{key}")
    public JsonResponse<String> getBpmnXml(@PathVariable String key) {
        return JsonResponse.success(modelService.findBpmnXmlById(key));
    }

    @PostMapping("/add")
    @SaCheckPermission("workflow_model_add")
    public JsonResponse<Object> save(@RequestBody SysFlowableModelCreateVO vo) {
        modelService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("workflow_model_update")
    public JsonResponse<Object> update(@RequestBody SysFlowableModelModifyVO vo) {
        modelService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/design")
    @SaCheckPermission("manage_workflow_design")
    public JsonResponse<Object> design(@RequestBody SysFlowableModelDesignVO vo) {
        modelService.design(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/deploy/{key}")
    @SaCheckPermission("manage_workflow_deploy")
    public JsonResponse<Object> deploy(@PathVariable String key) {
        modelService.deploy(getLoginCode(), key);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{id}")
    @SaCheckPermission("manage_workflow_delete")
    public JsonResponse<Object> delete(@PathVariable String id) {
        modelService.delete(getLoginCode(), id);
        return JsonResponse.success();
    }
}
