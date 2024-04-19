package com.lego.flowable.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.vo.FlowableModelDesignVO;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class DesignFlowableModelAction extends MaintainAction {

    private FlowableModelDesignVO vo;
    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);

    public DesignFlowableModelAction(String operatorCode, FlowableModelDesignVO vo) {
        super(SysPermissionCode.manageWorkFlowModel, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getId()), "模型ID不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getBpmnXml()), "模型设计BpmnXml报文不能为空！");
    }

    @Override
    protected void doRun() {
        Model model = repositoryService.createModelQuery()
            .modelId(vo.getId())
            .singleResult();
        BusinessException.check(model != null, "不存在的模型[{0}]！", vo.getId());

        Integer latestVersion = repositoryService.createModelQuery()
            .modelKey(model.getKey())
            .latestVersion()
            .singleResult()
            .getVersion();
        Model newModel = repositoryService.newModel();
        newModel.setName(model.getName());
        newModel.setKey(model.getKey());
        newModel.setCategory(model.getCategory());
        newModel.setMetaInfo(model.getMetaInfo());
        newModel.setVersion(latestVersion + 1);
        repositoryService.saveModel(newModel);

        byte[] bpmnXmlBytes = StringUtil.getBytes(vo.getBpmnXml(), StandardCharsets.UTF_8);
        repositoryService.addModelEditorSource(newModel.getId(), bpmnXmlBytes);
        this.description = MessageFormat.format("更新模型[{0}]的Bpmn设计图", vo.getId());
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return MessageFormat.format("模型[{0}]", vo.getId());
    }
}
