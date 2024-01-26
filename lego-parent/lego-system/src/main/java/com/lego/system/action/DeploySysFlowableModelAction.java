package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.common.ProcessConstants;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;

import java.text.MessageFormat;

public class DeploySysFlowableModelAction extends MaintainAction {

    private String key;

    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);
    private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);

    public DeploySysFlowableModelAction(String operatorCode, String key) {
        super(SysPermissionCode.manage, operatorCode);
        this.key = key;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(key), "模型标识不能为空！");
    }

    @Override
    protected void doRun() {
        ModelQuery modelQuery = repositoryService.createModelQuery()
            .modelKey(key)
            .latestVersion()
            .orderByCreateTime()
            .desc();
        Model model = modelQuery.singleResult();
        BusinessException.check(model != null, "不存在的模型[{0}]！", key);

        SysCustomForm form = formDao.findByCode(model.getKey());
        byte[] bpmnBytes = repositoryService.getModelEditorSource(model.getId());
        String processName = model.getName() + ProcessConstants.SUFFIX;
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
        form.setDefinitionId(processDef.getId());
        this.description = MessageFormat.format("发布模型[{0}]", key);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }
}
