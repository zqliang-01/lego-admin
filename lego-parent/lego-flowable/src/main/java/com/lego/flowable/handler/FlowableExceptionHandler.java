package com.lego.flowable.handler;

import com.lego.core.common.ExceptionEnum;
import com.lego.core.web.ILegoExceptionHandler;
import org.flowable.common.engine.api.FlowableException;
import org.springframework.stereotype.Component;

@Component
public class FlowableExceptionHandler implements ILegoExceptionHandler {

    @Override
    public boolean accept(Throwable e) {
        return e instanceof FlowableException;
    }

    @Override
    public int getCode(Throwable e) {
        return ExceptionEnum.BUSINESS_INVALID.getCode();
    }

    @Override
    public String getMessage(Throwable e) {
        return e.getMessage();
    }
}
