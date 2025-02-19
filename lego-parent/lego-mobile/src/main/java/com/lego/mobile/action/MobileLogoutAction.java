package com.lego.mobile.action;

import cn.dev33.satoken.stp.StpUtil;
import com.lego.core.action.MaintainAction;
import com.lego.core.enums.ActionType;
import com.lego.core.util.StringUtil;
import com.lego.mobile.dao.IMobileUserBindDao;
import com.lego.mobile.entity.MobileUserBind;

public class MobileLogoutAction extends MaintainAction {

    private String token;

    private IMobileUserBindDao bindDao = getDao(IMobileUserBindDao.class);

    public MobileLogoutAction(String operatorCode, String token) {
        super("manage", operatorCode);
        this.token = token;
    }

    @Override
    protected void doRun() {
        if (StpUtil.isLogin()) {
            MobileUserBind userBind = bindDao.findByEmployee(operatorCode, StringUtil.getMD5(token));
            if (userBind != null) {
                userBind.expired();
                bindDao.save(userBind);
            }
            StpUtil.logout();
        }
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return "注销登录";
    }
}
