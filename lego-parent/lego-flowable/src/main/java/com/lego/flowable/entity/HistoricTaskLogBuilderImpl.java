package com.lego.flowable.entity;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.service.TaskServiceConfiguration;
import org.flowable.task.service.impl.HistoricTaskLogEntryBuilderImpl;

public class HistoricTaskLogBuilderImpl extends HistoricTaskLogEntryBuilderImpl {

    public HistoricTaskLogBuilderImpl(CommandExecutor commandExecutor, TaskServiceConfiguration taskServiceConfiguration) {
        super(commandExecutor, taskServiceConfiguration);
    }

    public HistoricTaskLogBuilderImpl(CommandExecutor commandExecutor, TaskInfo task, TaskServiceConfiguration taskServiceConfiguration) {
        super(commandExecutor, task, taskServiceConfiguration);
    }

    @Override
    public Void execute(CommandContext commandContext) {
        if (StringUtils.isEmpty(getTaskId())) {
            throw new FlowableIllegalArgumentException("Empty taskId is not allowed for HistoricTaskLogEntry");
        }
        if (StringUtils.isEmpty(getUserId())) {
            userId(Authentication.getAuthenticatedUserId());
        }
        if (timeStamp == null) {
            timeStamp(taskServiceConfiguration.getClock().getCurrentTime());
        }

        taskServiceConfiguration.getHistoricTaskLogEntryEntityManager().createHistoricTaskLogEntry(this);
        return null;
    }

}
