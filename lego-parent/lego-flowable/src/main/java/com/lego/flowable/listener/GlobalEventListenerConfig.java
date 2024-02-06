package com.lego.flowable.listener;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 全局监听配置
 */
@Configuration
public class GlobalEventListenerConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private GlobalEventListener globalEventListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        runtimeService.addEventListener(globalEventListener,
            FlowableEngineEventType.PROCESS_COMPLETED,
            FlowableEngineEventType.TASK_COMPLETED);
    }
}
