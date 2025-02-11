package com.lego.system.action;

import com.lego.core.action.DeleteAction;
import com.lego.system.dao.ISysNoticeDao;
import com.lego.system.entity.SysNotice;
import com.lego.system.vo.SysPermissionCode;

public class DeleteSysNoticeAction extends DeleteAction<SysNotice, ISysNoticeDao> {

    public DeleteSysNoticeAction(String operatorCode, String entityCode) {
        super(SysPermissionCode.manage, operatorCode, entityCode);
    }

    @Override
    protected void createLog() {
    }
}
