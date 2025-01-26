package com.lego.core.module.flowable.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.lego.core.common.Constants;
import com.lego.core.data.ICommonService;
import com.lego.core.exception.BusinessException;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.module.flowable.IFlowableCompletedListener;
import com.lego.core.module.flowable.IFlowableCompletedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowableCompletedService implements IFlowableCompletedService {

    @Autowired
    private ICommonService commonService;

    @Autowired(required = false)
    private List<IFlowableCompletedListener> listeners;

    @Override
    public String taskCompleted(TaskCompletedVO vo) {
        String permissionCode = commonService.findPermissionCodeByTable(vo.getTableCode());
        if (!StpUtil.getRoleList().contains(Constants.ADMIN_ROLE_CODE)) {
            StpUtil.checkPermission(permissionCode);
        }
        return getListener(vo.getTableCode()).taskCompleted(vo.getVariable());
    }

    @Override
    public void taskRejected(String tableCode, String code) {
        getListener(tableCode).taskRejected(code);
    }

    @Override
    public void processCompleted(String tableCode, String code) {
        getListener(tableCode).processCompleted(code);
    }

    private IFlowableCompletedListener getListener(String tableCode) {
        BusinessException.check(listeners != null, "数据表[{0}]无匹配的任务完工处理器，任务处理失败！", tableCode);
        for (IFlowableCompletedListener listener : listeners) {
            if (listener.getTableCode().equals(tableCode)) {
                return listener;
            }
        }
        throw new BusinessException("数据表[{0}]无匹配的任务完工处理器，任务处理失败！", tableCode);
    }
}
