package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.MessageCreateVO;
import com.lego.system.dao.ISysMessageDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysMessage;
import com.lego.system.entity.simpletype.SysMessageType;
import com.lego.system.vo.SysPermissionCode;

public class AddSysMessageAction extends AddAction<SysMessage, ISysMessageDao> {

    private MessageCreateVO vo;

    public AddSysMessageAction(String operatorCode, MessageCreateVO vo) {
        super(SysPermissionCode.manage, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "消息标题不能为空，消息推送失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getType()), "消息类型不能为空，消息推送失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getRecipient()), "消息接收人不能为空，消息推送失败！");
    }

    @Override
    protected SysMessage createTargetEntity() {
        SysMessageType type = findByCode(SysMessageType.class, vo.getType());
        SysEmployee creator = findByUnsureCode(SysEmployee.class, vo.getCreator());
        SysEmployee recipient = findByCode(SysEmployee.class, vo.getRecipient());

        SysMessage message = new SysMessage(vo.getName(), type, creator, recipient);
        message.setContent(vo.getContent());
        message.setEntityCode(vo.getEntityCode());
        message.setForm(findByUnsureCode(SysCustomForm.class, vo.getFormCode()));
        return message;
    }

    @Override
    protected void createLog() {
    }
}
