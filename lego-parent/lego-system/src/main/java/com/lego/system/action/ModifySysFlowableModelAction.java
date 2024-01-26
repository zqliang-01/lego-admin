package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.system.vo.SysFlowableModelModifyVO;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;

public class ModifySysFlowableModelAction extends MaintainAction {

    private SysFlowableModelModifyVO vo;

    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);

    public ModifySysFlowableModelAction(String operatorCode, SysFlowableModelModifyVO vo) {
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
        ModelQuery modelQuery = repositoryService.createModelQuery()
            .modelKey(vo.getKey())
            .latestVersion()
            .orderByCreateTime()
            .desc();
        Model model = modelQuery.singleResult();
        BusinessException.check(model != null, "不存在的模型[{0}]！", vo.getKey());

        model.setName(vo.getName());
        model.setCategory(vo.getCategory());
        model.setMetaInfo(vo.getDescription());
        repositoryService.saveModel(model);
        this.description = "更新工作流模型：" + vo.toString();
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ADD;
    }
}
