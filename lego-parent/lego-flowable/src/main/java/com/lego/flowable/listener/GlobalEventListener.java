package com.lego.flowable.listener;

import cn.hutool.core.bean.BeanUtil;
import com.lego.core.exception.BusinessException;
import com.lego.core.flowable.FlowableTaskCompletedListener;
import com.lego.core.flowable.IFlowableProcessCompletedListener;
import com.lego.core.util.StringUtil;
import com.lego.flowable.vo.ProcessStatus;
import com.lego.system.service.ISysCustomFormService;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 监听处理完工
 */
@Component
public class GlobalEventListener extends AbstractFlowableEngineEventListener {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ISysCustomFormService formService;
    @Autowired(required = false)
    private List<FlowableTaskCompletedListener> taskListeners;
    @Autowired(required = false)
    private List<IFlowableProcessCompletedListener> processListeners;

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
            runtimeService.updateBusinessStatus(event.getProcessInstanceId(), ProcessStatus.COMPLETED.getCode());
        }
        super.processCompleted(event);
    }

    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
        FlowableEntityEventImpl variableEvent = (FlowableEntityEventImpl) event;
        TaskEntityImpl task = (TaskEntityImpl) variableEvent.getEntity();
        if (taskListeners != null && StringUtil.isNotBlank(task.getFormKey())) {
            String tableCode = formService.findTableCodeBy(task.getFormKey());
            for (FlowableTaskCompletedListener taskListener : taskListeners) {
                if (tableCode.equals(taskListener.getTableCode())) {
                    Object vo = BeanUtil.toBean(task.getVariables(), taskListener.getClassType());
                    taskListener.doCompleted(vo);
                }
            }
        }
    }
}
