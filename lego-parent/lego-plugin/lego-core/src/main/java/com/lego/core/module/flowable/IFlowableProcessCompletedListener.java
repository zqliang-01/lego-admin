package com.lego.core.module.flowable;

public interface IFlowableProcessCompletedListener {

    void doCompleted(String processInstanceId);
}
