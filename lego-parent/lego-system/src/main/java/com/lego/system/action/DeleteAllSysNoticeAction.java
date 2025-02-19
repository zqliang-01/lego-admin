package com.lego.system.action;

import com.lego.core.action.EntityAction;
import com.lego.core.enums.ActionType;
import com.lego.system.dao.ISysNoticeDao;
import com.lego.system.entity.SysNotice;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;

public class DeleteAllSysNoticeAction extends EntityAction<SysNotice, ISysNoticeDao> {

    private String type;

    public DeleteAllSysNoticeAction(String operatorCode, String type) {
        super(SysPermissionCode.manage, operatorCode);
        this.type = type;
    }

    @Override
    protected void doRun() {
        List<SysNotice> messages = entityDao.findBy(operatorCode, true);
        entityDao.deleteAllInBatch(messages);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.DELETE;
    }
}
