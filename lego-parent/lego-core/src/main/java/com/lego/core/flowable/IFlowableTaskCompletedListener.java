package com.lego.core.flowable;

interface IFlowableTaskCompletedListener<T> {

    void doCompleted(T vo);

    Class<T> getClassType();

    String getFormKey();
}
