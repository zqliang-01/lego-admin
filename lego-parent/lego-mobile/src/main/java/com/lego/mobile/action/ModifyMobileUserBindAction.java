package com.lego.mobile.action;

import com.lego.core.action.ModifyAction;
import com.lego.mobile.dao.IMobileUserBindDao;
import com.lego.mobile.entity.MobileUserBind;

public class ModifyMobileUserBindAction extends ModifyAction<MobileUserBind, IMobileUserBindDao> {

    private String token;

    public ModifyMobileUserBindAction(String operatorCode, String entityCode, String token) {
        super("manage", operatorCode, entityCode);
        this.token = token;
    }

    @Override
    protected void doModify(MobileUserBind entity) {
        entity.setEmployeeCode(operatorCode);
        entity.refreshExpired(token);
    }
}
