package com.lego.system.action;

import com.lego.core.action.EntityAction;
import com.lego.core.enums.ActionType;
import com.lego.system.dao.ISysMessageDao;
import com.lego.system.entity.SysMessage;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;

public class ReadAllSysMessageAction extends EntityAction<SysMessage, ISysMessageDao> {

    private String type;

    public ReadAllSysMessageAction(String operatorCode, String type) {
        super(SysPermissionCode.manage, operatorCode);
        this.type = type;
    }

    @Override
    protected void doRun() {
        List<SysMessage> messages = entityDao.findUnReadBy(operatorCode, type);
        messages.stream().forEach(message -> message.setReaded(true));
        entityDao.saveAll(messages);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected void createLog() {
    }
}
