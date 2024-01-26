package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.system.vo.SysFlowableModelDesignVO;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class DesignSysFlowableModelAction extends MaintainAction {

    private SysFlowableModelDesignVO vo;
    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);

    public DesignSysFlowableModelAction(String operatorCode, SysFlowableModelDesignVO vo) {
        super(SysPermissionCode.manage, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getKey()), "模型标识不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getBpmnXml()), "模型设计BpmnXml报文不能为空！");
    }

    @Override
    protected void doRun() {
        ModelQuery modelQuery = repositoryService.createModelQuery()
            .modelKey(vo.getKey())
            .latestVersion()
            .orderByCreateTime()
            .desc();
        Model model = modelQuery.singleResult();
        BusinessException.check(model != null, "不存在的模型[{0}]！", vo.getKey());

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
        this.description = MessageFormat.format("更新模型[{0}]的Bpmn设计图", vo.getKey());
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }
}
