package com.lego.flowable.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.entity.HistoricTaskLogBuilderImpl;
import org.flowable.common.engine.impl.AbstractServiceConfiguration;
import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.common.engine.impl.interceptor.EngineConfigurationConstants;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.history.HistoricTaskLogEntryBuilder;
import org.flowable.task.service.TaskServiceConfiguration;

public abstract class FlowableAction extends MaintainAction {


    protected TaskService taskService = LegoBeanFactory.getBean(TaskService.class);
    protected RepositoryService repositoryService = LegoBeanFactory.getBean(RepositoryService.class);

    protected FlowableAction(String permissionCode, String operatorCode) {
        super(permissionCode, operatorCode);
    }

    protected String getNextId() {
        ProcessEngine engine = LegoBeanFactory.getBean(ProcessEngine.class);
        return engine.getProcessEngineConfiguration().getIdGenerator().getNextId();
    }

    protected void addTaskLog(TaskInfo task, String type, String data) {
        ProcessEngine engine = LegoBeanFactory.getBean(ProcessEngine.class);
        CommandExecutor commandExecutor = engine.getProcessEngineConfiguration().getCommandExecutor();
        AbstractServiceConfiguration serviceConfiguration = engine.getProcessEngineConfiguration()
            .getServiceConfigurations().get(EngineConfigurationConstants.KEY_TASK_SERVICE_CONFIG);
        TaskServiceConfiguration taskServiceConfiguration = (TaskServiceConfiguration) serviceConfiguration;
        HistoricTaskLogEntryBuilder taskLog = new HistoricTaskLogBuilderImpl(commandExecutor, task, taskServiceConfiguration);
        taskLog.type(type).data(data).create();
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

}
