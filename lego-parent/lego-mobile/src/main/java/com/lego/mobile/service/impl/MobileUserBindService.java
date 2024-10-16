package com.lego.mobile.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.mobile.action.AddMobileUserBindAction;
import com.lego.mobile.action.ModifyMobileUserBindAction;
import com.lego.mobile.dao.IMobileAppConfigDao;
import com.lego.mobile.dao.IMobileUserBindDao;
import com.lego.mobile.entity.MobileAppConfig;
import com.lego.mobile.entity.MobileUserBind;
import com.lego.mobile.entity.simpletype.MobileAppType;
import com.lego.mobile.service.IMobileUserBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileUserBindService extends BaseService implements IMobileUserBindService {

    @Autowired
    private IMobileUserBindDao userBindDao;

    @Autowired
    private IMobileAppConfigDao appConfigDao;

    @Override
    public void update(String operatorCode, String appCode, String openid, String token) {
        MobileAppConfig appConfig = appConfigDao.findByCode(appCode);
        MobileAppType appType = appConfig.getType();
        MobileUserBind userBind = userBindDao.findBy(openid, appType.getCode());
        if (userBind == null) {
            new AddMobileUserBindAction(operatorCode, openid, appType, token).run();
            return;
        }
        new ModifyMobileUserBindAction(operatorCode, userBind.getCode(), token).run();
    }
}
