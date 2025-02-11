package com.lego.flowable.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.web.LegoBeanFactory;
import org.flowable.engine.RepositoryService;

import java.text.MessageFormat;

public class DeleteFlowableModelAction extends MaintainAction {

    private String id;

    private RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);

    public DeleteFlowableModelAction(String operatorCode, String id) {
        super("manage_workflow", operatorCode);
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

    @Override
    protected String getEntityName() {
        return MessageFormat.format("模型[{0}]", id);
    }
}
