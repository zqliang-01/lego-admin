package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.util.DateUtil;
import com.lego.system.dao.ISysMessageDao;
import com.lego.system.entity.SysMessage;
import com.lego.system.vo.SysPermissionCode;

public class ReadSysMessageAction extends ModifyAction<SysMessage, ISysMessageDao> {

    public ReadSysMessageAction(String operatorCode, String entityCode) {
        super(SysPermissionCode.manage, operatorCode, entityCode);
    }

    @Override
    protected void doModify(SysMessage entity) {
        entity.setReaded(true);
        entity.setReadTime(DateUtil.currentDateTime());
    }

    @Override
    protected void createLog() {
    }
}
