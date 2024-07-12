package com.lego.flowable.config;

import org.flowable.common.engine.impl.AbstractServiceConfiguration;
import org.flowable.engine.ProcessEngine;
import org.flowable.task.service.TaskServiceConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class FlowableConfiguration implements InitializingBean {

    @Autowired
    private ProcessEngine processEngine;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, AbstractServiceConfiguration> configs = processEngine.getProcessEngineConfiguration().getServiceConfigurations();
        for (AbstractServiceConfiguration value : configs.values()) {
            if (value instanceof TaskServiceConfiguration) {
                TaskServiceConfiguration taskServiceConfiguration = (TaskServiceConfiguration) value;
                taskServiceConfiguration.setEnableHistoricTaskLogging(true);
            }
        }
    }
}
