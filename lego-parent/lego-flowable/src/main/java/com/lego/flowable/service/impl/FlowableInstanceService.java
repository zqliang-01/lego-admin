package com.lego.flowable.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.StringUtil;
import com.lego.flowable.action.StopFlowableInstanceAction;
import com.lego.flowable.assembler.FlowableInstanceAssembler;
import com.lego.flowable.assembler.FlowableModelAssembler;
import com.lego.flowable.dto.FlowableInstanceInfo;
import com.lego.flowable.dto.FlowableProcessNodeInfo;
import com.lego.flowable.dto.IFlowableStartFormDetailInfo;
import com.lego.flowable.service.IFlowableInstanceService;
import com.lego.flowable.vo.FlowableInstanceSearchVO;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
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
            historicProcessInstanceQuery.processInstanceNameLike("%" + vo.getName() + "%");
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
        if (CollectionUtil.isEmpty(allActivityInstanceList)) {
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
                if (BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW.equals(item.getActivityType())
                    || BpmnXMLConstants.ELEMENT_FLOW_CONDITION.equals(item.getActivityType())) {
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
        nodeInfo.setName(instance.getName());
        InputStream bpmnBytes = repositoryService.getProcessModel(instance.getProcessDefinitionId());
        nodeInfo.setXml(StringUtil.toString(bpmnBytes));

        BpmnModel bpmnModel = repositoryService.getBpmnModel(instance.getProcessDefinitionId());
        nodeInfo.setRejectedTaskSet(modelAssembler.getRejectFlow(bpmnModel, nodeInfo.getFinishedTaskSet(), nodeInfo.getFinishedSequenceFlowSet(), runningTaskSet));
        return nodeInfo;
    }

    @Override
    public void stop(String operatorCode, String id) {
        new StopFlowableInstanceAction(operatorCode, id).run();
    }

    @Override
    public IFlowableStartFormDetailInfo findStartForm(String id) {
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(id)
            .singleResult();
        String code = processInstance.getBusinessKey();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        String formKey = modelAssembler.getStartEvent(bpmnModel).getFormKey();
        return new IFlowableStartFormDetailInfo(formKey, code);
    }

    @Override
    public void downloadImage(HttpServletResponse response, String id) {
        // 构建查询条件
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(id);
        List<HistoricActivityInstance> allActivityInstanceList = query.list();
        if (CollectionUtil.isEmpty(allActivityInstanceList)) {
            return;
        }
        List<String> finishedTaskSet = new ArrayList<>();
        List<String> finishedSequenceFlowSet = new ArrayList<>();
        // 查询所有已完成的元素
        List<HistoricActivityInstance> finishedElementList = allActivityInstanceList.stream().collect(Collectors.toList());
        finishedElementList.forEach(item -> {
            if (BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW.equals(item.getActivityType())
                || BpmnXMLConstants.ELEMENT_FLOW_CONDITION.equals(item.getActivityType())) {
                finishedSequenceFlowSet.add(item.getActivityId());
            } else {
                finishedTaskSet.add(item.getActivityId());
            }
        });

        HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(id)
            .singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(instance.getProcessDefinitionId());
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engineConfig.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", finishedTaskSet, finishedSequenceFlowSet, engineConfig.getActivityFontName(), engineConfig.getLabelFontName(), engineConfig.getAnnotationFontName(), engineConfig.getClassLoader(), 1.0, true);
        ServletUtil.write(response, in);
    }
}
