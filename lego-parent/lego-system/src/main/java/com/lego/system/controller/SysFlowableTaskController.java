package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysFlowableTaskInfo;
import com.lego.system.service.ISysFlowableTaskService;
import com.lego.system.vo.SysFlowableTaskStartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-end/sys-flowable-task")
public class SysFlowableTaskController extends BaseController {

    @Autowired
    private ISysFlowableTaskService taskService;

    @PostMapping("/start")
    @SaCheckPermission("workflow_task_start")
    public JsonResponse<String> start(@RequestBody SysFlowableTaskStartVO vo) {
        String instanceId = taskService.start(getLoginCode(), vo.getDefinitionId(), vo.getVariables());
        return JsonResponse.success(instanceId);
    }

    @PostMapping("/list-undo/{definitionId}")
    public JsonResponse<SysFlowableTaskInfo> findUndo(@PathVariable String definitionId) {
        SysFlowableTaskInfo taskInfo = taskService.findUndoBy(getLoginCode(), definitionId);
        return JsonResponse.success(taskInfo);
    }

    @PostMapping("/complete/{id}")
    public JsonResponse<Object> complete(@PathVariable String id) {
        taskService.complete(getLoginCode(), id);
        return JsonResponse.success();
    }

    @GetMapping("/get-bpmn-xml/{definitionId}")
    public JsonResponse<Object> getBpmnXml(@PathVariable String definitionId) {
        String bpmnXml = taskService.getBpmnXml(definitionId);
        return JsonResponse.success(bpmnXml);
    }

}
