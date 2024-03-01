package com.lego.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.flowable.FlowableService;
import com.lego.core.util.StringUtil;
import com.lego.flowable.assembler.FlowableInstanceAssembler;
import com.lego.flowable.assembler.FlowableModelAssembler;
import com.lego.flowable.dto.FlowableInstanceInfo;
import com.lego.flowable.dto.FlowableProcessNodeInfo;
import com.lego.flowable.service.IFlowableInstanceService;
import com.lego.flowable.vo.FlowableInstanceSearchVO;
import com.lego.flowable.vo.ProcessStatus;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FlowableInstanceService extends FlowableService<FlowableInstanceAssembler> implements IFlowableInstanceService {

    @Autowired
    private FlowableModelAssembler modelAssembler;

    @Override
    public LegoPage<FlowableInstanceInfo> findBy(String operatorCode, FlowableInstanceSearchVO vo) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
            .startedBy(operatorCode)
            .orderByProcessInstanceStartTime()
            .desc();
        if (StringUtil.isNotBlank(vo.getName())) {
            historicProcessInstanceQuery.processDefinitionName(vo.getName());
        }
        LegoPage<HistoricProcessInstance> page = createPage(historicProcessInstanceQuery, vo, HistoricProcessInstance.class);
        return assembler.create(page);
    }

    @Override
    public FlowableProcessNodeInfo findProcessNodeBy(String id) {
        FlowableProcessNodeInfo nodeInfo = new FlowableProcessNodeInfo();
        // 构建查询条件
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(id);
        List<HistoricActivityInstance> allActivityInstanceList = query.list();
        if (CollUtil.isEmpty(allActivityInstanceList)) {
            return nodeInfo;
        }
        Set<String> runningTaskSet = taskService.createTaskQuery()
            .processInstanceId(id)
            .list()
            .stream()
            .map(Task::getTaskDefinitionKey)
            .collect(Collectors.toSet());
        // 查询所有已完成的元素
        List<HistoricActivityInstance> finishedElementList = allActivityInstanceList.stream()
            .filter(item -> item.getEndTime() != null).collect(Collectors.toList());
        finishedElementList.forEach(item -> {
            if (!runningTaskSet.contains(item.getActivityId())) {
                if (BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW.equals(item.getActivityType())) {
                    nodeInfo.addFinishedFlow(item.getActivityId());
                } else {
                    nodeInfo.addFinishedTask(item.getActivityId());
                }
            }
        });
        nodeInfo.setUnfinishedTaskSet(runningTaskSet);

        HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(id)
            .singleResult();
        InputStream bpmnBytes = repositoryService.getProcessModel(instance.getProcessDefinitionId());
        nodeInfo.setXml(StringUtil.toString(bpmnBytes));

        BpmnModel bpmnModel = repositoryService.getBpmnModel(instance.getProcessDefinitionId());
        nodeInfo.setRejectedTaskSet(modelAssembler.getRejectTask(bpmnModel, nodeInfo.getFinishedTaskSet(), nodeInfo.getFinishedSequenceFlowSet(), runningTaskSet));
        return nodeInfo;
    }

    @Override
    public String getBpmnXml(String id) {
        HistoricProcessInstance hisInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
        String definitionId = hisInstance.getProcessDefinitionId();
        if (definitionId == "") {
            return null;
        }
        //获得活动的节点
        List<HistoricActivityInstance> historyProcess = historyService.createHistoricActivityInstanceQuery().processInstanceId(id).orderByHistoricActivityInstanceStartTime().asc().list();
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        for (HistoricActivityInstance hi : historyProcess) {
            String activityType = hi.getActivityType();
            if (BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW.equals(activityType)) {
                flows.add(hi.getActivityId());
            } else {
                activityIds.add(hi.getActivityId());
            }
        }
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(id).list();
        for (Task runningTask : tasks) {
            activityIds.add(runningTask.getTaskDefinitionKey());
        }
        ProcessEngineConfiguration engConf = processEngine.getProcessEngineConfiguration();
        engConf.setActivityFontName("宋体");
        engConf.setLabelFontName("宋体");
        engConf.setAnnotationFontName("宋体");
        //定义流程画布生成器
        ProcessDiagramGenerator processDiagramGenerator = engConf.getProcessDiagramGenerator();
        InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engConf.getActivityFontName()
            , engConf.getLabelFontName(), engConf.getAnnotationFontName(), engConf.getClassLoader(), 1.0, true);
        return StringUtil.encodeBase64(IoUtil.readBytes(in));
    }

    @Override
    public void stop(String operatorCode, String id) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(id)
            .singleResult();
        BusinessException.check(processInstance != null, "不存在有效的流程实例[{0}]，实例终止失败！", id);
        ProcessStatus status = ProcessStatus.get(processInstance.getBusinessStatus());
        BusinessException.check(status.isRunning(), "流程[{0}]非运行中状态，流程终止失败！", id);

        runtimeService.updateBusinessStatus(id, ProcessStatus.TERMINATED.getCode());

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        EndEvent endEvent = modelAssembler.getEndEvent(bpmnModel);
        List<Execution> executions = runtimeService.createExecutionQuery().parentId(id).list();
        List<String> executionIds = executions.stream().map(Execution::getId).collect(Collectors.toList());
        executions.forEach(execution -> executionIds.add(execution.getId()));
        runtimeService.createChangeActivityStateBuilder()
            .processInstanceId(id)
            .moveExecutionsToSingleActivityId(executionIds, endEvent.getId())
            .changeState();
    }
}
