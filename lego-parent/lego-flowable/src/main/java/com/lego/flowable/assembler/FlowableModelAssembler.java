package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.flowable.dto.FlowableModelInfo;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.engine.repository.Model;
import org.springframework.stereotype.Component;

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

    public StartEvent getStartEvent(BpmnModel model) {
        Process process = model.getMainProcess();
        FlowElement startElement = process.getInitialFlowElement();
        if (startElement instanceof StartEvent) {
            return (StartEvent) startElement;
        }
        for (FlowElement flowElement : process.getFlowElements()) {
            if (flowElement instanceof StartEvent) {
                return (StartEvent) flowElement;
            }
        }
        return null;
    }

}
