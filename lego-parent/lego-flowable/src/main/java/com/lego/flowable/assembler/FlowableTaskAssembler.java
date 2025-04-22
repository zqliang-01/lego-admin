package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.DateUtil;
import com.lego.flowable.dto.FlowableTaskInfo;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class FlowableTaskAssembler extends BaseAssembler<FlowableTaskInfo, Task> {

    @Resource
    protected TaskService taskService;
    @Resource
    protected HistoryService historyService;

    private void addExtendParam(FlowableTaskInfo taskInfo, TaskInfo task) {
        HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(task.getProcessInstanceId())
            .singleResult();
        taskInfo.setStartUser(createEmployee(instance.getStartUserId()));
        taskInfo.setDefinitionName(instance.getName());
    }

    @Override
    protected FlowableTaskInfo doCreate(Task entity) {
        FlowableTaskInfo info = new FlowableTaskInfo();
        info.setId(entity.getId());
        info.setKey(entity.getTaskDefinitionKey());
        info.setName(entity.getName());
        info.setAssignee(createEmployee(entity.getAssignee()));
        info.setOwner(createEmployee(entity.getOwner()));
        info.setFormCode(entity.getFormKey());
        info.setInstanceId(entity.getProcessInstanceId());
        info.setCreateTime(entity.getCreateTime());
        addExtendParam(info, entity);
        return info;
    }

    public FlowableTaskInfo createHis(HistoricTaskInstance task) {
        FlowableTaskInfo info = new FlowableTaskInfo();
        info.setId(task.getId());
        info.setKey(task.getTaskDefinitionKey());
        info.setName(task.getName());
        info.setAssignee(createEmployee(task.getAssignee()));
        info.setOwner(createEmployee(task.getOwner()));
        info.setFormCode(task.getFormKey());
        info.setInstanceId(task.getProcessInstanceId());
        info.setCreateTime(task.getCreateTime());
        info.setEndTime(task.getEndTime());
        Date endTime = DateUtil.currentDateTime();
        if (task.getEndTime() != null) {
            endTime = task.getEndTime();
        }
        info.setDuration(DateUtil.getDatePoor(endTime, task.getStartTime()));
        addExtendParam(info, task);
        return info;
    }

    public List<FlowableTaskInfo> createHis(List<HistoricTaskInstance> tasks) {
        List<FlowableTaskInfo> infos = new ArrayList<>();
        for (HistoricTaskInstance task : tasks) {
            infos.add(createHis(task));
        }
        return infos;
    }

    public LegoPage<FlowableTaskInfo> createHis(LegoPage<HistoricTaskInstance> page) {
        List<FlowableTaskInfo> infos = new ArrayList<>();
        for (HistoricTaskInstance entity : page.getResult()) {
            infos.add(createHis(entity));
        }
        return new LegoPage<FlowableTaskInfo>(infos, page.getPageIndex(), page.getPageSize(), page.getTotalCount());
    }
}
