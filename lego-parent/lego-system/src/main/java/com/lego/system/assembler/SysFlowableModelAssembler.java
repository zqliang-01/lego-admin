package com.lego.system.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.system.dto.SysFlowableModelInfo;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.engine.repository.Model;
import org.springframework.stereotype.Component;

@Component
public class SysFlowableModelAssembler extends BaseAssembler<SysFlowableModelInfo, Model> {

    private final BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();

    @Override
    protected SysFlowableModelInfo doCreate(Model entity) {
        SysFlowableModelInfo info = new SysFlowableModelInfo();
        info.setId(entity.getId());
        info.setKey(entity.getKey());
        info.setName(entity.getName());
        info.setVersion(entity.getVersion());
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
