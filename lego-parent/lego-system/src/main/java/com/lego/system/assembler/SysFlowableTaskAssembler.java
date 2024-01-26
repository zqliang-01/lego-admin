package com.lego.system.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.system.dto.SysFlowableTaskInfo;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

@Component
public class SysFlowableTaskAssembler extends BaseAssembler<SysFlowableTaskInfo, Task> {

    @Override
    protected SysFlowableTaskInfo doCreate(Task entity) {
        SysFlowableTaskInfo info = new SysFlowableTaskInfo();
        info.setId(entity.getId());
        info.setKey(entity.getTaskDefinitionKey());
        info.setName(entity.getName());
        info.setAssignee(entity.getAssignee());
        info.setOwner(entity.getOwner());
        return info;
    }
}
