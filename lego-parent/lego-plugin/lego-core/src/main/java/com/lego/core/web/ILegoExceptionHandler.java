package com.lego.core.web;

public interface ILegoExceptionHandler {

    boolean accept(Throwable e);

    int getCode(Throwable e);

    String getMessage(Throwable e);
}
