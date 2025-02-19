package com.lego.mobile.action;

import cn.dev33.satoken.stp.StpUtil;
import com.lego.core.action.MaintainAction;
import com.lego.core.enums.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.mobile.dao.IMobileUserBindDao;
import com.lego.mobile.entity.MobileUserBind;
import com.lego.mobile.entity.simpletype.MobileAppType;

public class MobileAutoLoginAction extends MaintainAction {

    private MobileAppType type;
    private MobileUserBind userBind;

    private IMobileUserBindDao userBindDao = getDao(IMobileUserBindDao.class);

    public MobileAutoLoginAction(String operatorCode, MobileUserBind userBind) {
        super("manage", operatorCode);
        this.type = userBind.getType();
        this.userBind = userBind;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(operatorCode), "登录账号不能为空！");
    }

    @Override
    protected void doRun() {
        StpUtil.login(operatorCode);
        userBind.setToken(StpUtil.getTokenValue());
        userBindDao.save(userBind);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.LOGIN;
    }

    @Override
    protected String getEntityName() {
        return "账号自动登录[" + EntityUtil.getName(type) + "]";
    }
}
