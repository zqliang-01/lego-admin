package com.lego.flowable.controller;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.flowable.dto.FlowableProcessNodeInfo;
import com.lego.flowable.dto.FlowableTaskFormDetailInfo;
import com.lego.flowable.dto.FlowableTaskInfo;
import com.lego.flowable.service.IFlowableTaskService;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.flowable.vo.FlowableTaskSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-end/flowable-task")
public class FlowableTaskController extends BaseController {

    @Autowired
    private IFlowableTaskService taskService;

    @PostMapping("/list-undo")
    public JsonResponse<LegoPage<FlowableTaskInfo>> findUndo(@RequestBody FlowableTaskSearchVO vo) {
        LegoPage<FlowableTaskInfo> tasks = taskService.findUndoBy(getLoginCode(), vo);
        return JsonResponse.success(tasks);
    }

    @PostMapping("/list-completed")
    public JsonResponse<LegoPage<FlowableTaskInfo>> findCompleted(@RequestBody FlowableTaskSearchVO vo) {
        LegoPage<FlowableTaskInfo> tasks = taskService.findCompletedBy(getLoginCode(), vo);
        return JsonResponse.success(tasks);
    }

    @PostMapping("/complete")
    public JsonResponse<Object> complete(@RequestBody FlowableTaskCompleteVO vo) {
        taskService.complete(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @GetMapping("/get-bpmn-xml/{instanceId}")
    public JsonResponse<Object> getBpmnXml(@PathVariable String instanceId) {
        String bpmnXml = taskService.getBpmnXml(instanceId);
        return JsonResponse.success(bpmnXml);
    }

    @GetMapping("/list-process-node/{instanceId}")
    public JsonResponse<FlowableProcessNodeInfo> listProcessNode(@PathVariable String instanceId) {
        FlowableProcessNodeInfo nodeInfo = taskService.findProcessNodeBy(instanceId);
        return JsonResponse.success(nodeInfo);
    }

    @GetMapping("/get-form-detail/{id}")
    public JsonResponse<FlowableTaskFormDetailInfo> getFormDetail(@PathVariable String id) {
        FlowableTaskFormDetailInfo info = taskService.findCodeVariable(id);
        return JsonResponse.success(info);
    }
}
