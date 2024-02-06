package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.core.dto.LegoPage;
import com.lego.flowable.dto.FlowableTaskInfo;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlowableTaskAssembler extends BaseAssembler<FlowableTaskInfo, Task> {

    @Override
    protected FlowableTaskInfo doCreate(Task entity) {
        FlowableTaskInfo info = new FlowableTaskInfo();
        info.setId(entity.getId());
        info.setKey(entity.getTaskDefinitionKey());
        info.setName(entity.getName());
        info.setAssignee(entity.getAssignee());
        info.setOwner(entity.getOwner());
        info.setFormCode(entity.getFormKey());
        info.setInstanceId(entity.getProcessInstanceId());
        info.setCreateTime(entity.getCreateTime());
        return info;
    }

    public LegoPage<FlowableTaskInfo> createHisPage(LegoPage<HistoricTaskInstance> page) {
        List<FlowableTaskInfo> infos = new ArrayList<>();
        for (HistoricTaskInstance entity : page.getResult()) {
            FlowableTaskInfo info = new FlowableTaskInfo();
            info.setId(entity.getId());
            info.setKey(entity.getTaskDefinitionKey());
            info.setName(entity.getName());
            info.setAssignee(entity.getAssignee());
            info.setOwner(entity.getOwner());
            info.setFormCode(entity.getFormKey());
            info.setInstanceId(entity.getProcessInstanceId());
            info.setCreateTime(entity.getCreateTime());
            info.setEndTime(entity.getEndTime());
            infos.add(info);
        }
        return new LegoPage<FlowableTaskInfo>(infos, page.getPageIndex(), page.getPageSize(), page.getTotalCount());
    }
}
