package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.core.util.DateUtil;
import com.lego.flowable.dto.FlowableInstanceInfo;
import com.lego.flowable.vo.ProcessStatus;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FlowableInstanceAssembler extends BaseAssembler<FlowableInstanceInfo, HistoricProcessInstance> {

    @Override
    protected FlowableInstanceInfo doCreate(HistoricProcessInstance entity) {
        FlowableInstanceInfo info = new FlowableInstanceInfo();
        info.setId(entity.getId());
        info.setName(entity.getName());
        info.setStatus(ProcessStatus.getTypeInfo(entity.getBusinessStatus()));
        info.setVersion("v" + entity.getProcessDefinitionVersion());
        info.setStartTime(entity.getStartTime());
        info.setEndTime(entity.getEndTime());
        Date endTime = DateUtil.currentDateTime();
        if (entity.getEndTime() != null) {
            endTime = entity.getEndTime();
        }
        info.setDuration(DateUtil.getDatePoor(endTime, entity.getStartTime()));
        info.setStartUser(createEmployee(entity.getStartUserId()));
        return info;
    }
}
