package com.lego.system.service.impl;

import com.lego.core.data.FlowableService;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.action.AddSysFlowableModelAction;
import com.lego.system.action.DeleteSysFlowableModelAction;
import com.lego.system.action.DeploySysFlowableModelAction;
import com.lego.system.action.DesignSysFlowableModelAction;
import com.lego.system.action.ModifySysFlowableModelAction;
import com.lego.system.assembler.SysFlowableDefinitionAssembler;
import com.lego.system.assembler.SysFlowableModelAssembler;
import com.lego.system.dto.SysFlowableDefinitionInfo;
import com.lego.system.dto.SysFlowableModelInfo;
import com.lego.system.service.ISysFlowableModelService;
import com.lego.system.vo.SysFlowableModelCreateVO;
import com.lego.system.vo.SysFlowableModelDesignVO;
import com.lego.system.vo.SysFlowableModelModifyVO;
import com.lego.system.vo.SysFlowableModelSearchVO;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysFlowableModelService extends FlowableService<SysFlowableModelAssembler> implements ISysFlowableModelService {

    @Autowired
    private SysFlowableDefinitionAssembler definitionAssembler;

    @Override
    public LegoPage<SysFlowableModelInfo> findBy(SysFlowableModelSearchVO vo) {
        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByCreateTime().desc();
        if (StringUtil.isNotBlank(vo.getModelName())) {
            modelQuery.modelNameLike("%" + vo.getModelName() + "%");
        }
        LegoPage<Model> page = createPage(modelQuery, vo, Model.class);
        return assembler.create(page);
    }

    @Override
    public List<SysFlowableDefinitionInfo> findDefinitionBy(String definitionId) {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(definitionId)
            .singleResult();
        BusinessException.check(definition != null, "不存在的流程定义[{0}]", definitionId);

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(definition.getKey())
            .active()
            .orderByProcessDefinitionVersion()
            .asc();
        List<ProcessDefinition> definitionList = processDefinitionQuery.list();
        return definitionAssembler.create(definitionList);
    }

    @Override
    public String findBpmnXmlById(String key) {
        ModelQuery modelQuery = repositoryService.createModelQuery()
            .modelKey(key)
            .latestVersion()
            .orderByCreateTime()
            .desc();
        Model model = modelQuery.singleResult();
        BusinessException.check(model != null, "不存在的模型[{0}]！", key);

        byte[] bpmnBytes = repositoryService.getModelEditorSource(model.getId());
        return StringUtil.toString(bpmnBytes);
    }

    @Override
    public void add(String operatorCode, SysFlowableModelCreateVO vo) {
        new AddSysFlowableModelAction(operatorCode, vo).run();
    }

    @Override
    public void design(String operatorCode, SysFlowableModelDesignVO vo) {
        new DesignSysFlowableModelAction(operatorCode, vo).run();
    }

    @Override
    public void deploy(String operatorCode, String key) {
        new DeploySysFlowableModelAction(operatorCode, key).run();
    }

    @Override
    public void delete(String operatorCode, String id) {
        new DeleteSysFlowableModelAction(operatorCode, id).run();
    }

    @Override
    public void update(String operatorCode, SysFlowableModelModifyVO vo) {
        new ModifySysFlowableModelAction(operatorCode, vo).run();
    }
}
