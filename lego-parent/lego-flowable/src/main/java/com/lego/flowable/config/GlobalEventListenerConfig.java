package com.lego.flowable.config;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.data.ICommonService;
import com.lego.core.dto.FormInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.module.flowable.FlowableProcessConstants;
import com.lego.core.util.StringUtil;
import com.lego.flowable.assembler.FlowableModelAssembler;
import com.lego.flowable.handler.IFlowableCompleteHandler;
import com.lego.flowable.vo.FlowableTaskLogType;
import com.lego.flowable.vo.ProcessStatus;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskLogEntry;
import org.flowable.task.api.history.HistoricTaskLogEntryQuery;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 全局监听配置
 */
@Configuration
public class GlobalEventListenerConfig extends AbstractFlowableEngineEventListener implements InitializingBean {

    @Autowired
    private RuntimeService runtimeService;
    @Resource
    protected HistoryService historyService;
    @Resource
    protected RepositoryService repositoryService;
    @Autowired
    private FlowableModelAssembler modelAssembler;
    @Autowired
    private IFlowableCompleteHandler completeHandler;
    @Autowired
    private ICommonService commonService;

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

        String businessStatus = instance.getBusinessStatus();
        processStartFormCallback(instance, businessStatus);
        if (ProcessStatus.RUNNING.getCode().equals(businessStatus)) {
            processBusinessCompletedCallback(instance);
            runtimeService.updateBusinessStatus(event.getProcessInstanceId(), ProcessStatus.COMPLETED.getCode());
        } else if (ProcessStatus.TERMINATED.getCode().equals(businessStatus)) {
            processBusinessRejectedCallback(instance);
        }
        super.processCompleted(event);
    }

    /**
     * 该部分逻辑会查询历史流程任务，存在耗时可能，后续自行增加表单处理信息存储进行回调
     */
    private void processBusinessCompletedCallback(ProcessInstance instance) {
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
            .includeTaskLocalVariables()
            .processInstanceId(instance.getProcessInstanceId())
            .taskWithFormKey()
            .taskWithoutDeleteReason()
            .orderByHistoricTaskInstanceEndTime()
            .desc()
            .list();
        String logType = FlowableTaskLogType.REJECT.getCode();
        HistoricTaskLogEntryQuery taskLogQuery = historyService.createHistoricTaskLogEntryQuery();
        for (HistoricTaskInstance task : tasks) {
            if (StringUtil.isNotBlank(task.getDeleteReason())) {
                continue;
            }
            Object uniqueValue = task.getTaskLocalVariables().get(FlowableProcessConstants.FORM_UNIQUE_KEY);
            String code = StringUtil.toString(uniqueValue);
            if (StringUtil.isBlank(code)) {
                continue;
            }
            List<HistoricTaskLogEntry> taskLogs = taskLogQuery.taskId(task.getId()).type(logType).list();
            FormInfo form = commonService.findFormBy(task.getFormKey());
            if (CollectionUtil.isEmpty(taskLogs)) {
                completeHandler.doProcessCompleted(form.getAppCode(), form.getTableCode(), code);
            } else {
                completeHandler.doTaskRejected(form.getAppCode(), form.getTableCode(), code);
            }
        }
    }

    private void processBusinessRejectedCallback(ProcessInstance instance) {
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
            .includeTaskLocalVariables()
            .processInstanceId(instance.getProcessInstanceId())
            .taskWithFormKey()
            .taskWithoutDeleteReason()
            .orderByHistoricTaskInstanceEndTime()
            .desc()
            .list();
        for (HistoricTaskInstance task : tasks) {
            Object uniqueValue = task.getTaskLocalVariables().get(FlowableProcessConstants.FORM_UNIQUE_KEY);
            String code = StringUtil.toString(uniqueValue);
            if (StringUtil.isBlank(code)) {
                continue;
            }
            FormInfo form = commonService.findFormBy(task.getFormKey());
            completeHandler.doTaskRejected(form.getAppCode(), form.getTableCode(), code);
        }
    }

    private void processStartFormCallback(ProcessInstance instance, String status) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(instance.getProcessDefinitionId());
        String formKey = modelAssembler.getStartEvent(bpmnModel).getFormKey();
        if (StringUtil.isNotBlank(formKey)) {
            String code = instance.getBusinessKey();
            FormInfo form = commonService.findFormBy(formKey);
            if (ProcessStatus.RUNNING.getCode().equals(instance.getBusinessStatus())) {
                completeHandler.doProcessCompleted(form.getAppCode(), form.getTableCode(), code);
            } else {
                completeHandler.doTaskRejected(form.getAppCode(), form.getTableCode(), code);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        runtimeService.addEventListener(this, FlowableEngineEventType.PROCESS_COMPLETED);
    }
}
