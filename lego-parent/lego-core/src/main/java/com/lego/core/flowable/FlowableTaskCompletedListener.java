package com.lego.core.flowable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class FlowableTaskCompletedListener<T> implements IFlowableTaskCompletedListener<T> {

    @Override
    public Class<T> getClassType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type type = actualTypeArguments[0];
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getRawType();
        }
        return (Class<T>) type;
    }
}
