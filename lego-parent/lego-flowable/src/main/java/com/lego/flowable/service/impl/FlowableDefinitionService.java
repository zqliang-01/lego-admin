package com.lego.flowable.service.impl;

import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.flowable.action.StartFlowableTaskAction;
import com.lego.flowable.assembler.FlowableDefinitionAssembler;
import com.lego.flowable.assembler.FlowableModelAssembler;
import com.lego.flowable.dto.FlowableDefinitionInfo;
import com.lego.flowable.mapper.FlowableDefinitionMapper;
import com.lego.flowable.service.IFlowableDefinitionService;
import com.lego.flowable.vo.FlowableDefinitionSearchVO;
import com.lego.flowable.vo.FlowableTaskStartVO;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Arrays;

@Service
public class FlowableDefinitionService extends FlowableService<FlowableDefinitionAssembler> implements IFlowableDefinitionService {

    @Autowired
    private FlowableDefinitionMapper definitionMapper;

    @Autowired
    private FlowableModelAssembler modelAssembler;

    @Override
    public LegoPage<FlowableDefinitionInfo> findBy(FlowableDefinitionSearchVO vo) {
        IPage<FlowableDefinitionInfo> page = new Page(vo.getPageIndex(), vo.getPageSize());
        page = definitionMapper.selectLatestByName(vo.getName(), page);
        return new LegoPage<>(page);
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
    public void start(String operatorCode, FlowableTaskStartVO vo) {
        new StartFlowableTaskAction(operatorCode, vo).run();
    }

    @Override
    public String findStartFormKey(String id) {
        BpmnModel model = repositoryService.getBpmnModel(id);
        StartEvent startEvent = modelAssembler.getStartEvent(model);
        return startEvent.getFormKey();
    }

    @Override
    public void downloadImage(HttpServletResponse response, String id) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(id);
        ProcessEngineConfiguration engine = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engine.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", Arrays.asList(), Arrays.asList(), engine.getActivityFontName(), engine.getLabelFontName(), engine.getAnnotationFontName(), engine.getClassLoader(), 1.0, true);
        ServletUtil.write(response, in);
    }

}
