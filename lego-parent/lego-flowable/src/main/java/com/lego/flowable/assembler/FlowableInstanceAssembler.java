package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.core.util.DateUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.flowable.dto.FlowableInstanceInfo;
import com.lego.flowable.vo.ProcessStatus;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class FlowableInstanceAssembler extends BaseAssembler<FlowableInstanceInfo, HistoricProcessInstance> {

    @Resource
    protected RepositoryService repositoryService;

    @Override
    protected FlowableInstanceInfo doCreate(HistoricProcessInstance entity) {
        Deployment deployment = repositoryService.createDeploymentQuery()
            .deploymentId(entity.getDeploymentId())
            .singleResult();
        FlowableInstanceInfo info = new FlowableInstanceInfo();
        info.setId(entity.getId());
        info.setName(deployment.getName());
        info.setStatus(ProcessStatus.getTypeInfo(entity.getBusinessStatus()));
        info.setVersion("v" + entity.getProcessDefinitionVersion());
        info.setStartTime(entity.getStartTime());
        info.setEndTime(entity.getEndTime());
        Date endTime = DateUtil.currentDateTime();
        if (entity.getEndTime() != null) {
            endTime = entity.getEndTime();
        }
        info.setDuration(DateUtil.getDatePoor(endTime, entity.getStartTime()));
        info.setStartUser(create(CustomFieldTypeEnum.EMPLOYEE, entity.getStartUserId()));
        return info;
    }
}
