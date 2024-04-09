package com.lego.system.action;

import com.lego.core.action.EntityAction;
import com.lego.core.data.ActionType;
import com.lego.system.dao.ISysMessageDao;
import com.lego.system.entity.SysMessage;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;

public class DeleteAllSysMessageAction extends EntityAction<SysMessage, ISysMessageDao> {

    private String type;

    public DeleteAllSysMessageAction(String operatorCode, String type) {
        super(SysPermissionCode.manage, operatorCode);
        this.type = type;
    }

    @Override
    protected void doRun() {
        List<SysMessage> messages = entityDao.findBy(operatorCode, type, true);
        entityDao.deleteAllInBatch(messages);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.DELETE;
    }
}
