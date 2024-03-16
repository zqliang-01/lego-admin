package com.lego.flowable.service.impl;

import com.lego.core.dto.LegoPage;
import com.lego.core.util.StringUtil;
import com.lego.flowable.action.AddFlowableModelAction;
import com.lego.flowable.action.DeleteFlowableModelAction;
import com.lego.flowable.action.DeployFlowableModelAction;
import com.lego.flowable.action.DesignFlowableModelAction;
import com.lego.flowable.action.ModifyFlowableModelAction;
import com.lego.flowable.assembler.FlowableDefinitionAssembler;
import com.lego.flowable.assembler.FlowableModelAssembler;
import com.lego.flowable.dto.FlowableModelInfo;
import com.lego.flowable.service.IFlowableModelService;
import com.lego.flowable.vo.FlowableModelCreateVO;
import com.lego.flowable.vo.FlowableModelDesignVO;
import com.lego.flowable.vo.FlowableModelModifyVO;
import com.lego.flowable.vo.FlowableModelSearchVO;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowableModelService extends FlowableService<FlowableModelAssembler> implements IFlowableModelService {

    @Autowired
    private FlowableDefinitionAssembler definitionAssembler;

    @Override
    public LegoPage<FlowableModelInfo> findBy(FlowableModelSearchVO vo) {
        ModelQuery modelQuery = repositoryService.createModelQuery()
            .latestVersion()
            .orderByCreateTime()
            .desc();
        if (StringUtil.isNotBlank(vo.getName())) {
            modelQuery.modelNameLike("%" + vo.getName() + "%");
        }
        LegoPage<Model> page = createPage(modelQuery, vo, Model.class);
        return assembler.create(page);
    }

    @Override
    public LegoPage<FlowableModelInfo> findHistBy(FlowableModelSearchVO vo) {
        ModelQuery modelQuery = repositoryService.createModelQuery()
            .modelKey(vo.getKey())
            .orderByModelVersion()
            .desc();
        if (StringUtil.isNotBlank(vo.getName())) {
            modelQuery.modelNameLike("%" + vo.getName() + "%");
        }
        LegoPage<Model> page = createPage(modelQuery, vo, Model.class);
        return assembler.create(page);
    }

    @Override
    public String findBpmnXmlById(String id) {
        byte[] bpmnBytes = repositoryService.getModelEditorSource(id);
        return StringUtil.toString(bpmnBytes);
    }

    @Override
    public void add(String operatorCode, FlowableModelCreateVO vo) {
        new AddFlowableModelAction(operatorCode, vo).run();
    }

    @Override
    public void design(String operatorCode, FlowableModelDesignVO vo) {
        new DesignFlowableModelAction(operatorCode, vo).run();
    }

    @Override
    public void deploy(String operatorCode, String id) {
        new DeployFlowableModelAction(operatorCode, id).run();
    }

    @Override
    public void delete(String operatorCode, String id) {
        new DeleteFlowableModelAction(operatorCode, id).run();
    }

    @Override
    public void update(String operatorCode, FlowableModelModifyVO vo) {
        new ModifyFlowableModelAction(operatorCode, vo).run();
    }
}
