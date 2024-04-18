package com.lego.system.action;

import com.lego.core.action.EntityAction;
import com.lego.core.data.ActionType;
import com.lego.system.dao.ISysNoticeDao;
import com.lego.system.entity.SysNotice;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;

public class ReadAllSysNoticeAction extends EntityAction<SysNotice, ISysNoticeDao> {

    private String type;

    public ReadAllSysNoticeAction(String operatorCode, String type) {
        super(SysPermissionCode.manage, operatorCode);
        this.type = type;
    }

    @Override
    protected void doRun() {
        List<SysNotice> notices = entityDao.findUnReadBy(operatorCode);
        notices.stream().forEach(notice -> notice.setReaded(true));
        entityDao.saveAll(notices);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected void createLog() {
    }
}
