package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;
import com.lego.flowable.dto.FlowableModelInfo;
import com.lego.flowable.dto.FlowableProcessNodeInfo;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.repository.Model;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class FlowableModelAssembler extends BaseAssembler<FlowableModelInfo, Model> {

    private final BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();

    @Override
    protected FlowableModelInfo doCreate(Model entity) {
        FlowableModelInfo info = new FlowableModelInfo();
        info.setId(entity.getId());
        info.setKey(entity.getKey());
        info.setName(entity.getName());
        info.setVersion("v" + entity.getVersion());
        info.setCategory(entity.getCategory());
        info.setCreateTime(entity.getCreateTime());
        info.setDescription(entity.getMetaInfo());
        return info;
    }

    public String getUserTaskAttributeValue(BpmnModel model, String taskKey, String name) {
        UserTask element = getUserTaskByKey(model, taskKey);
        return element.getAttributeValue(BpmnXMLConstants.FLOWABLE_EXTENSIONS_NAMESPACE, name);
    }

    public UserTask getUserTaskByKey(BpmnModel model, String taskKey) {
        Process process = model.getMainProcess();
        FlowElement flowElement = process.getFlowElement(taskKey);
        if (flowElement instanceof UserTask) {
            return (UserTask) flowElement;
        }
        throw new BusinessException("不存在的UserTask标识[{0}]", taskKey);
    }

    public List<String> getBeforeUserTaskId(FlowNode userTask, List<String> finishedTaskIds) {
        List<String> taskIds = new ArrayList<>();
        for (SequenceFlow incomingFlow : userTask.getIncomingFlows()) {
            FlowElement flowElement = incomingFlow.getSourceFlowElement();
            if ((flowElement instanceof UserTask || flowElement instanceof StartEvent)
                && finishedTaskIds.contains(flowElement.getId())) {
                taskIds.add(flowElement.getId());
            } else if (flowElement instanceof FlowNode) {
                taskIds.addAll(getBeforeUserTaskId((FlowNode) flowElement, finishedTaskIds));
            }
        }
        return taskIds;
    }

    public EndEvent getEndEvent(BpmnModel bpmnModel) {
        Process process = bpmnModel.getMainProcess();
        for (FlowElement flowElement : process.getFlowElements()) {
            if (flowElement instanceof EndEvent) {
                ((EndEvent) flowElement).getOutgoingFlows();
                return (EndEvent) flowElement;
            }
        }
        throw new BusinessException("流程定义[{0}]未获取到结束节点！", process.getName());
    }

    public Set<String> getRejectTask(BpmnModel bpmnModel, Set<String> finishedTaskSet, Set<String> finishedSequenceFlowSet, Set<String> runningTaskSet) {
        Set<String> rejectTaskSet = new HashSet<>();
        Process process = bpmnModel.getMainProcess();
        for (String taskKey : runningTaskSet) {
            FlowElement flowElement = process.getFlowElement(taskKey);
            iteratorFindRejects(flowElement, finishedTaskSet, finishedSequenceFlowSet, rejectTaskSet);
            if (!rejectTaskSet.isEmpty() && finishedTaskSet.contains(taskKey)) {
                rejectTaskSet.add(taskKey);
            }
        }
        return rejectTaskSet;
    }

    public void iteratorFindRejects(FlowElement source, Set<String> finishedTaskSet, Set<String> finishedSequenceFlowSet, Set<String> rejectedSet) {
        List<SequenceFlow> sequenceFlows = getElementOutgoingFlows(source);
        for (SequenceFlow sequenceFlow: sequenceFlows) {
            FlowElement targetElement = sequenceFlow.getTargetFlowElement();
            if (finishedTaskSet.contains(targetElement.getId())) {
                rejectedSet.add(targetElement.getId());
            }
            // 添加未完成的连线
            if (finishedSequenceFlowSet.contains(sequenceFlow.getId())) {
                rejectedSet.add(sequenceFlow.getId());
            }
            // 如果节点为子流程节点情况，则从节点中的第一个节点开始获取
            if (targetElement instanceof SubProcess) {
                SubProcess subProcess = (SubProcess) targetElement;
                for (FlowElement flowElement : subProcess.getFlowElements()) {
                    iteratorFindRejects(flowElement, finishedTaskSet, finishedSequenceFlowSet, rejectedSet);
                }
            }
            iteratorFindRejects(targetElement, finishedTaskSet, finishedSequenceFlowSet, rejectedSet);
        }
    }
    public List<SequenceFlow> getElementOutgoingFlows(FlowElement source) {
        List<SequenceFlow> sequenceFlows = new ArrayList<>();
        if (source instanceof FlowNode) {
            sequenceFlows = ((FlowNode) source).getOutgoingFlows();
        }
        return sequenceFlows;
    }
    public List<SequenceFlow> getElementIncomingFlows(FlowElement source) {
        List<SequenceFlow> sequenceFlows = new ArrayList<>();
        if (source instanceof FlowNode) {
            sequenceFlows = ((FlowNode) source).getIncomingFlows();
        }
        return sequenceFlows;
    }
}
