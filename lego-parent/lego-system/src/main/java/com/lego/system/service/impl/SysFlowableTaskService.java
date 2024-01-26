package com.lego.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.data.FlowableService;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.system.assembler.SysFlowableTaskAssembler;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dto.SysFlowableTaskInfo;
import com.lego.system.entity.SysEmployee;
import com.lego.system.service.ISysFlowableTaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class SysFlowableTaskService extends FlowableService<SysFlowableTaskAssembler> implements ISysFlowableTaskService {

    @Autowired
    private ISysCustomFormDao formDao;

    @Autowired
    private ISysEmployeeDao employeeDao;

    @Override
    public String start(String operatorCode, String definitionId, Map<String, Object> variables) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(definitionId)
            .latestVersion()
            .active();
        ProcessDefinition definition = processDefinitionQuery.singleResult();
        BusinessException.check(definition != null, "模型[{0}]不存在正常可用的流程定义，启动流程失败！", definitionId);

        identityService.setAuthenticatedUserId(operatorCode);
        // 发起流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId(), variables);
        return instance.getId();
    }

    @Override
    public SysFlowableTaskInfo findUndoBy(String employeeCode, String definitionId) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        List<String> candidateGroups = EntityUtil.getCode(employee.getRoles());
        candidateGroups.add(EntityUtil.getCode(employee.getDept()));
        TaskQuery taskQuery = taskService.createTaskQuery()
            .processDefinitionId(definitionId)
            .active()
            .taskCandidateOrAssigned(employeeCode)
            .taskCandidateGroupIn(candidateGroups)
            .orderByTaskCreateTime().desc();
        List<Task> tasks = taskQuery.list();
        if (CollectionUtil.isEmpty(tasks)) {
            return null;
        }
        return assembler.create(tasks.stream().findFirst().get());
    }

    @Override
    public void complete(String employeeCode, String id) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        List<String> candidateGroups = EntityUtil.getCode(employee.getRoles());
        candidateGroups.add(EntityUtil.getCode(employee.getDept()));
        TaskQuery taskQuery = taskService.createTaskQuery()
            .taskId(id)
            .active()
            .taskCandidateOrAssigned(employeeCode)
            .taskCandidateGroupIn(candidateGroups)
            .orderByTaskCreateTime().desc();
        Task task = taskQuery.singleResult();

        BusinessException.check(task != null, "当前任务审核人非[{0}]，审核失败！", employeeCode);
        taskService.complete(id);
    }

    @Override
    public String getBpmnXml(String definitionId) {
        InputStream inputStream = repositoryService.getProcessModel(definitionId);
        return StringUtil.toString(inputStream);
    }

}
