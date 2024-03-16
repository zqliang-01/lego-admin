package com.lego.core.data.hibernate;

public interface IBusService {

    void updateCheckStatus(String code, String checkStatus);

    boolean exists(String code);
}
