package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.system.vo.SysFlowableModelCreateVO;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;

public class AddSysFlowableModelAction extends MaintainAction {

    private SysFlowableModelCreateVO vo;

    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);

    public AddSysFlowableModelAction(String operatorCode, SysFlowableModelCreateVO vo) {
        super(SysPermissionCode.manage, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getKey()), "模型标识不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "模型名称不能为空！");
    }

    @Override
    protected void doRun() {
        Integer latestVersion = repositoryService.createModelQuery()
            .modelKey(vo.getKey())
            .latestVersion()
            .singleResult()
            .getVersion();

        Model model = repositoryService.newModel();
        model.setKey(vo.getKey());
        model.setName(vo.getName());
        model.setCategory(vo.getCategory());
        model.setMetaInfo(vo.getDescription());
        model.setVersion(latestVersion + 1);
        repositoryService.saveModel(model);
        this.description = "新增工作流模型：" + vo.toString();
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ADD;
    }
}
