package com.lego.core.flowable;

import java.util.Map;

public interface IFlowableTaskCompletedListener {

    void completed(boolean isSave, Map<String, Object> variables);

    void processCompleted(String code);

    String getTableCode();
}
