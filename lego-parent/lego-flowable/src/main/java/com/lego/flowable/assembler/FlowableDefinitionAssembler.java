package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.flowable.dto.FlowableDefinitionInfo;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Component;

@Component
public class FlowableDefinitionAssembler extends BaseAssembler<FlowableDefinitionInfo, ProcessDefinition> {

    @Override
    protected FlowableDefinitionInfo doCreate(ProcessDefinition entity) {
        FlowableDefinitionInfo info = new FlowableDefinitionInfo();
        info.setId(entity.getId());
        info.setKey(entity.getKey());
        info.setName(entity.getName());
        info.setCategory(entity.getCategory());
        info.setVersion("v" + entity.getVersion());
        info.setActive(!entity.isSuspended());
        info.setDeploymentId(entity.getDeploymentId());
        return info;
    }
}
