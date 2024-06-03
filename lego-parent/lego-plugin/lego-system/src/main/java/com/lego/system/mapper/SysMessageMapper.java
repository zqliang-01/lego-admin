package com.lego.system.mapper;

import com.lego.system.dto.SysMessageCountInfo;

public interface SysMessageMapper {

    SysMessageCountInfo selectUnreadCount(String operatorCode);
}
