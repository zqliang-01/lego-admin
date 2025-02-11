package com.lego.system.service;

import com.lego.core.dto.LegoPage;
import com.lego.system.dto.SysMessageCountInfo;
import com.lego.system.dto.SysMessageInfo;
import com.lego.system.vo.SysMessageSearchVO;

public interface ISysMessageService {

    void read(String operatorCode, String code);

    void readAll(String operatorCode, String type);

    LegoPage<SysMessageInfo> findBy(String operatorCode, SysMessageSearchVO vo);

    void delete(String operatorCode, String code);

    void deleteAll(String operatorCode, String type);

    SysMessageCountInfo findUnreadCountBy(String operatorCode);

    SysMessageInfo findByCode(String operatorCode, String code);
}
