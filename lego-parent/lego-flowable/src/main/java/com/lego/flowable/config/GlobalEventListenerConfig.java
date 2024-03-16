package com.lego.flowable.config;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.exception.BusinessException;
import com.lego.core.flowable.FlowableProcessConstants;
import com.lego.core.flowable.IFlowableProcessCompletedListener;
import com.lego.core.flowable.IFlowableTaskCompletedListener;
import com.lego.core.util.StringUtil;
import com.lego.flowable.vo.ProcessStatus;
import com.lego.system.service.ISysCustomFormService;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局监听配置
 */
@Configuration
public class GlobalEventListenerConfig extends AbstractFlowableEngineEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RuntimeService runtimeService;
    @Resource
    protected HistoryService historyService;
    @Autowired
    private ISysCustomFormService formService;
    @Autowired(required = false)
    private List<IFlowableTaskCompletedListener> taskListeners;
    @Autowired(required = false)
    private List<IFlowableProcessCompletedListener> processListeners;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        runtimeService.addEventListener(this, FlowableEngineEventType.PROCESS_COMPLETED);
    }

    /**
     * 流程结束监听器
     */
    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        String processInstanceId = event.getProcessInstanceId();
        if (processListeners != null) {
            for (IFlowableProcessCompletedListener processListener : processListeners) {
                processListener.doCompleted(processInstanceId);
            }
        }
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
        BusinessException.check(instance != null, "不存在有效的流程实例[{0}]，流程完工失败！", processInstanceId);

        if (ProcessStatus.RUNNING.getCode().equals(instance.getBusinessStatus())) {
            processBusinessCallback(processInstanceId);
            runtimeService.updateBusinessStatus(event.getProcessInstanceId(), ProcessStatus.COMPLETED.getCode());
        }
        super.processCompleted(event);
    }

    private void processBusinessCallback(String processInstanceId) {
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
            .includeTaskLocalVariables()
            .processInstanceId(processInstanceId)
            .taskWithFormKey()
            .taskWithoutDeleteReason()
            .orderByHistoricTaskInstanceEndTime()
            .desc()
            .list();
        List<String> keys = new ArrayList<>();
        for (HistoricTaskInstance task : tasks) {
            if (CollectionUtil.isEmpty(taskListeners)) {
                continue;
            }
            String formKey = task.getFormKey();
            String taskDefinitionKey = task.getTaskDefinitionKey();
            if (keys.contains(taskDefinitionKey + ":" + formKey)) {
                // TODO 该部分为被拒绝或多次创建的表单，后续考虑作废相关表单数据
                continue;
            }
            String tableCode = formService.findTableCodeBy(formKey);
            for (IFlowableTaskCompletedListener taskListener : taskListeners) {
                if (tableCode.equals(taskListener.getTableCode())) {
                    keys.add(taskDefinitionKey + ":" + formKey);
                    Object code = task.getTaskLocalVariables().get(FlowableProcessConstants.FORM_UNIQUE_KEY);
                    taskListener.processCompleted(StringUtil.toString(code));
                }
            }
        }
    }

}
