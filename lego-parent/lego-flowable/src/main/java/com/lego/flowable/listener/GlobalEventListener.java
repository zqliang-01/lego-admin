package com.lego.flowable.listener;

import cn.hutool.core.bean.BeanUtil;
import com.lego.core.flowable.FlowableTaskCompletedListener;
import com.lego.core.flowable.IFlowableProcessCompletedListener;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysCustomFormDao;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.impl.FlowableEntityWithVariablesEventImpl;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监听处理完工
 */
@Component
public class GlobalEventListener extends AbstractFlowableEngineEventListener {

    @Resource
    protected TaskService taskService;
    @Autowired(required = false)
    List<FlowableTaskCompletedListener> taskListeners;
    @Resource
    private ISysCustomFormDao formDao;
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
        super.processCompleted(event);
    }

    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
        FlowableEntityWithVariablesEventImpl variableEvent = (FlowableEntityWithVariablesEventImpl) event;
        Task task = (Task) variableEvent.getEntity();
        String formKey = task.getFormKey();
        if (taskListeners != null && StringUtil.isNotBlank(formKey)) {
            for (FlowableTaskCompletedListener taskListener : taskListeners) {
                if (formKey.equals(taskListener.getFormKey())) {
                    Object vo = BeanUtil.toBean(variableEvent.getVariables(), taskListener.getClassType());
                    taskListener.doCompleted(vo);
                }
            }
        }
    }
}
