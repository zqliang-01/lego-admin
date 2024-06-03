package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.util.DateUtil;
import com.lego.system.dao.ISysNoticeDao;
import com.lego.system.entity.SysNotice;
import com.lego.system.vo.SysPermissionCode;

public class ReadSysNoticeAction extends ModifyAction<SysNotice, ISysNoticeDao> {

    public ReadSysNoticeAction(String operatorCode, String entityCode) {
        super(SysPermissionCode.manage, operatorCode, entityCode);
    }

    @Override
    protected void doModify(SysNotice entity) {
        entity.setReaded(true);
        entity.setReadTime(DateUtil.currentDateTime());
    }

    @Override
    protected void createLog() {
    }
}
