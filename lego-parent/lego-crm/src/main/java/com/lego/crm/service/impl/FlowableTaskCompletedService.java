package com.lego.crm.service.impl;

import com.lego.core.common.AppCode;
import com.lego.core.exception.BusinessException;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.flowable.IFlowableTaskCompletedListener;
import com.lego.core.flowable.IFlowableTaskCompletedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowableTaskCompletedService implements IFlowableTaskCompletedService {

    @Autowired
    private List<IFlowableTaskCompletedListener> listeners;

    @Override
    public void completed(TaskCompletedVO vo) {
        IFlowableTaskCompletedListener listener = getListener(vo.getTableCode());
        listener.completed(vo.isSave(), vo.getVariable());
    }

    @Override
    public void processCompleted(String tableCode, String code) {
        IFlowableTaskCompletedListener listener = getListener(tableCode);
        listener.processCompleted(code);
    }

    @Override
    public boolean accept(String appCode) {
        return AppCode.crm.equals(appCode);
    }

    private IFlowableTaskCompletedListener getListener(String tableCode) {
        BusinessException.check(listeners != null, "数据表[{0}]无匹配的任务完工处理器，任务处理失败！", tableCode);
        for (IFlowableTaskCompletedListener listener : listeners) {
            if (listener.getTableCode().equals(tableCode)) {
                return listener;
            }
        }
        throw new BusinessException("数据表[{0}]无匹配的任务完工处理器，任务处理失败！", tableCode);
    }
}
