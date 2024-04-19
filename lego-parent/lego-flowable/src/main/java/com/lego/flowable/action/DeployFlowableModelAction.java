package com.lego.flowable.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.flowable.FlowableProcessConstants;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;

import java.text.MessageFormat;

public class DeployFlowableModelAction extends MaintainAction {

    private String id;

    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);
    private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);

    public DeployFlowableModelAction(String operatorCode, String id) {
        super(SysPermissionCode.manageWorkFlowModel, operatorCode);
        this.id = id;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(id), "模型标识不能为空！");
    }

    @Override
    protected void doRun() {
        Model model = repositoryService.createModelQuery()
            .modelId(id)
            .singleResult();
        BusinessException.check(model != null, "不存在的模型[{0}]！", id);

        byte[] bpmnBytes = repositoryService.getModelEditorSource(model.getId());
        BusinessException.check(bpmnBytes != null, "模型[{0}]尚未设计流程，部署失败！", id);
        String processName = model.getName() + FlowableProcessConstants.SUFFIX;
        // 部署流程
        Deployment deployment = repositoryService.createDeployment()
            .name(model.getName())
            .key(model.getKey())
            .category(model.getCategory())
            .addBytes(processName, bpmnBytes)
            .deploy();
        ProcessDefinition processDef = repositoryService.createProcessDefinitionQuery()
            .deploymentId(deployment.getId())
            .singleResult();
        repositoryService.setProcessDefinitionCategory(processDef.getId(), model.getCategory());
        this.description = MessageFormat.format("发布模型[{0}]", id);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return MessageFormat.format("模型[{0}]", id);
    }
}
