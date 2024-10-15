package com.lego.mobile.action;

import com.lego.core.action.AddAction;
import com.lego.mobile.dao.IMobileUserBindDao;
import com.lego.mobile.entity.MobileUserBind;
import com.lego.mobile.entity.simpletype.MobileAppType;

public class AddMobileUserBindAction extends AddAction<MobileUserBind, IMobileUserBindDao> {

    private String token;
    private String openid;
    private MobileAppType appType;

    public AddMobileUserBindAction(String operatorCode, String openid, MobileAppType appType, String token) {
        super("manage", operatorCode);
        this.token = token;
        this.openid = openid;
        this.appType = appType;
    }

    @Override
    protected MobileUserBind createTargetEntity() {
        MobileUserBind userBind = new MobileUserBind(openid, operatorCode, appType);
        userBind.setToken(token);
        return userBind;
    }
}
