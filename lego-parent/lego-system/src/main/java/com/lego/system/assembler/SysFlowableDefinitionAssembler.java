package com.lego.system.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.system.dto.SysFlowableDefinitionInfo;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Component;

@Component
public class SysFlowableDefinitionAssembler extends BaseAssembler<SysFlowableDefinitionInfo, ProcessDefinition> {

    @Override
    protected SysFlowableDefinitionInfo doCreate(ProcessDefinition entity) {
        SysFlowableDefinitionInfo info = new SysFlowableDefinitionInfo();
        info.setId(entity.getId());
        info.setKey(entity.getKey());
        info.setName(entity.getName());
        info.setCategory(entity.getCategory());
        info.setVersion(entity.getVersion());
        info.setSuspended(entity.isSuspended());
        info.setDeploymentId(entity.getDeploymentId());
        return info;
    }
}
