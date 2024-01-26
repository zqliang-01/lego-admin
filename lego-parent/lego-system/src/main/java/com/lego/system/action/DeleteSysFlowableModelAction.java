package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.web.LegoBeanFactory;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.engine.RepositoryService;

import java.text.MessageFormat;

public class DeleteSysFlowableModelAction extends MaintainAction {

    private String id;

    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);

    public DeleteSysFlowableModelAction(String operatorCode, String id) {
        super(SysPermissionCode.manageWorkFlow, operatorCode);
        this.id = id;
    }

    @Override
    protected void doRun() {
        repositoryService.deleteModel(id);
        this.description = MessageFormat.format("删除模型[{0}]", id);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.DELETE;
    }
}
