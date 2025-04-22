package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.flowable.dto.FlowableDefinitionInfo;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
public class FlowableDefinitionAssembler extends BaseAssembler<FlowableDefinitionInfo, ProcessDefinition> {

    @Resource
    private RepositoryService repositoryService;

    @Override
    protected FlowableDefinitionInfo doCreate(ProcessDefinition entity) {
        Deployment deployment = repositoryService.createDeploymentQuery()
            .deploymentId(entity.getDeploymentId())
            .singleResult();
        FlowableDefinitionInfo info = new FlowableDefinitionInfo();
        info.setId(entity.getId());
        info.setKey(entity.getKey());
        info.setName(deployment.getName());
        info.setCategory(entity.getCategory());
        info.setVersion("v" + entity.getVersion());
        info.setActive(!entity.isSuspended());
        info.setDeploymentTime(deployment.getDeploymentTime());
        return info;
    }
}
