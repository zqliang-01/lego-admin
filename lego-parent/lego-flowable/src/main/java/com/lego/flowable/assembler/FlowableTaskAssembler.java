package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.DateUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.flowable.dto.FlowableTaskInfo;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class FlowableTaskAssembler extends BaseAssembler<FlowableTaskInfo, Task> {

    @Resource
    protected RepositoryService repositoryService;
    @Resource
    protected HistoryService historyService;

    private void addExtendParam(FlowableTaskInfo taskInfo, String instanceId) {
        HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(instanceId)
            .singleResult();

        taskInfo.setStartUser(create(CustomFieldTypeEnum.EMPLOYEE, instance.getStartUserId()));
        Deployment deployment = repositoryService.createDeploymentQuery()
            .deploymentId(instance.getDeploymentId())
            .singleResult();
        taskInfo.setDefinitionName(deployment.getName());
    }

    @Override
    protected FlowableTaskInfo doCreate(Task entity) {
        FlowableTaskInfo info = new FlowableTaskInfo();
        info.setId(entity.getId());
        info.setKey(entity.getTaskDefinitionKey());
        info.setName(entity.getName());
        info.setAssignee(create(CustomFieldTypeEnum.EMPLOYEE, entity.getAssignee()));
        info.setOwner(create(CustomFieldTypeEnum.EMPLOYEE, entity.getOwner()));
        info.setFormCode(entity.getFormKey());
        info.setInstanceId(entity.getProcessInstanceId());
        info.setCreateTime(entity.getCreateTime());
        addExtendParam(info, entity.getProcessInstanceId());
        return info;
    }

    public FlowableTaskInfo createHis(HistoricTaskInstance task) {
        FlowableTaskInfo info = new FlowableTaskInfo();
        info.setId(task.getId());
        info.setKey(task.getTaskDefinitionKey());
        info.setName(task.getName());
        info.setAssignee(create(CustomFieldTypeEnum.EMPLOYEE, task.getAssignee()));
        info.setOwner(create(CustomFieldTypeEnum.EMPLOYEE, task.getOwner()));
        info.setFormCode(task.getFormKey());
        info.setInstanceId(task.getProcessInstanceId());
        info.setCreateTime(task.getCreateTime());
        info.setEndTime(task.getEndTime());
        Date endTime = DateUtil.currentDateTime();
        if (task.getEndTime() != null) {
            endTime = task.getEndTime();
        }
        info.setDuration(DateUtil.getDatePoor(endTime, task.getStartTime()));
        addExtendParam(info, task.getProcessInstanceId());
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
