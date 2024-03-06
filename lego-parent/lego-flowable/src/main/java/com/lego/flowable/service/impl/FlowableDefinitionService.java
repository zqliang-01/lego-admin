package com.lego.flowable.service.impl;

import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.flowable.FlowableService;
import com.lego.core.util.StringUtil;
import com.lego.flowable.assembler.FlowableDefinitionAssembler;
import com.lego.flowable.dto.FlowableDefinitionInfo;
import com.lego.flowable.service.IFlowableDefinitionService;
import com.lego.flowable.vo.FlowableDefinitionSearchVO;
import com.lego.flowable.vo.ProcessStatus;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
public class FlowableDefinitionService extends FlowableService<FlowableDefinitionAssembler> implements IFlowableDefinitionService {

    @Override
    public LegoPage<FlowableDefinitionInfo> findBy(FlowableDefinitionSearchVO vo) {
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
            .latestVersion()
            .orderByProcessDefinitionKey()
            .asc();
        if (StringUtil.isNotBlank(vo.getName())) {
            query.processDefinitionNameLike("%" + vo.getName() + "%");
        }
        LegoPage<ProcessDefinition> definitions = createPage(query, vo, ProcessDefinition.class);
        return assembler.create(definitions);
    }

    @Override
    public LegoPage<FlowableDefinitionInfo> findHistBy(FlowableDefinitionSearchVO vo) {
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(vo.getKey())
            .orderByProcessDefinitionVersion()
            .asc();
        if (StringUtil.isNotBlank(vo.getName())) {
            query.processDefinitionNameLike("%" + vo.getName() + "%");
        }
        LegoPage<ProcessDefinition> definitions = createPage(query, vo, ProcessDefinition.class);
        return assembler.create(definitions);
    }

    @Override
    public String findBpmnXmlById(String id) {
        InputStream inputStream = repositoryService.getProcessModel(id);
        return StringUtil.toString(inputStream);
    }

    @Override
    public void delete(String id) {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(id)
            .singleResult();
        BusinessException.check(definition != null, "不存在的流程定义[{0}]", id);
        repositoryService.deleteDeployment(definition.getDeploymentId(), true);
    }

    @Override
    public void updateStatus(String id, SuspensionState status) {
        if (SuspensionState.ACTIVE == status) {
            repositoryService.activateProcessDefinitionById(id, true, null);
            return;
        } else if (SuspensionState.SUSPENDED == status) {
            repositoryService.suspendProcessDefinitionById(id, true, null);
            return;
        }
        BusinessException.check(false, "未能识别的类型[{0}]，流程[{1}]状态修改失败！", status, id);
    }

    @Override
    public String start(String operatorCode, String definitionId, Map<String, Object> variables) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(definitionId)
            .latestVersion()
            .active();
        ProcessDefinition definition = processDefinitionQuery.singleResult();
        BusinessException.check(definition != null, "模型[{0}]不存在正常可用的流程定义，启动流程失败！", definitionId);

        Deployment deployment = repositoryService.createDeploymentQuery()
            .deploymentId(definition.getDeploymentId())
            .singleResult();

        identityService.setAuthenticatedUserId(operatorCode);
        variables.put(BpmnXMLConstants.ATTRIBUTE_EVENT_START_INITIATOR, operatorCode);

        // 构建发起流程实例
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        processInstanceBuilder.name(deployment.getName());
        processInstanceBuilder.processDefinitionId(definition.getId());
        processInstanceBuilder.variables(variables);
        processInstanceBuilder.businessStatus(ProcessStatus.RUNNING.getCode());
        ProcessInstance instance = processInstanceBuilder.start();
        return instance.getId();
    }

}
