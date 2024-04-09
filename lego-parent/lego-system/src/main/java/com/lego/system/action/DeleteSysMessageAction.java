package com.lego.system.action;

import com.lego.core.action.DeleteAction;
import com.lego.system.dao.ISysMessageDao;
import com.lego.system.entity.SysMessage;
import com.lego.system.vo.SysPermissionCode;

public class DeleteSysMessageAction extends DeleteAction<SysMessage, ISysMessageDao> {

    public DeleteSysMessageAction(String operatorCode, String entityCode) {
        super(SysPermissionCode.manage, operatorCode, entityCode);
    }

    @Override
    protected void createLog() {
    }
}
