package com.lego.flowable.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.vo.FlowableModelModifyVO;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;

import java.text.MessageFormat;

public class ModifyFlowableModelAction extends MaintainAction {

    private FlowableModelModifyVO vo;

    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);

    public ModifyFlowableModelAction(String operatorCode, FlowableModelModifyVO vo) {
        super(SysPermissionCode.manageWorkFlowModel, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getId()), "模型ID不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "模型名称不能为空！");
    }

    @Override
    protected void doRun() {
        Model model = repositoryService.createModelQuery()
            .modelId(vo.getId())
            .singleResult();
        BusinessException.check(model != null, "不存在的模型[{0}]！", vo.getId());

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

    @Override
    protected String getEntityName() {
        return MessageFormat.format("模型[{0}]", vo.getName());
    }
}
