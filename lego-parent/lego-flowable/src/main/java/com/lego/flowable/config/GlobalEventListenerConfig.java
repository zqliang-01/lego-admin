package com.lego.flowable.config;

import com.lego.core.exception.BusinessException;
import com.lego.core.flowable.FlowableProcessConstants;
import com.lego.core.util.StringUtil;
import com.lego.flowable.handler.FlowableTaskCompleteHandler;
import com.lego.flowable.vo.ProcessStatus;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysGenTable;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局监听配置
 */
@Configuration
public class GlobalEventListenerConfig extends AbstractFlowableEngineEventListener implements InitializingBean {

    @Autowired
    private ISysCustomFormDao formDao;
    @Autowired
    private RuntimeService runtimeService;
    @Resource
    protected HistoryService historyService;
    @Autowired
    private FlowableTaskCompleteHandler completeHandler;

    /**
     * 流程结束监听器
     */
    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        String processInstanceId = event.getProcessInstanceId();
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

    /**
     * 该部分逻辑会查询历史流程任务，存在耗时可能，后续自行增加表单处理信息存储进行回调
     */
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
            String formKey = task.getFormKey();
            String taskDefinitionKey = task.getTaskDefinitionKey();
            if (keys.contains(taskDefinitionKey + ":" + formKey)) {
                // TODO 该部分为被拒绝或多次创建的表单，后续考虑作废相关表单数据
                continue;
            }
            keys.add(taskDefinitionKey + ":" + formKey);
            SysCustomForm form = formDao.findByCode(formKey);
            SysGenTable table = form.getTable();
            String appCode = table.getAppCode();
            String tableCode = table.getCode();
            Object code = task.getTaskLocalVariables().get(FlowableProcessConstants.FORM_UNIQUE_KEY);
            completeHandler.doProcessCompleted(appCode, tableCode, StringUtil.toString(code));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        runtimeService.addEventListener(this, FlowableEngineEventType.PROCESS_COMPLETED);
    }
}
