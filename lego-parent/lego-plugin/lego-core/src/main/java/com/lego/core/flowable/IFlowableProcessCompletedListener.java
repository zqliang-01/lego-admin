package com.lego.core.flowable;

public interface IFlowableProcessCompletedListener {

    void doCompleted(String processInstanceId);
}
