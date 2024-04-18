package com.lego.system.service;

import com.lego.core.dto.LegoPage;
import com.lego.system.dto.SysNoticeInfo;
import com.lego.system.vo.SysNoticeSearchVO;

public interface ISysNoticeService {

    void read(String operatorCode, String code);

    void readAll(String operatorCode, String type);

    LegoPage<SysNoticeInfo> findBy(String operatorCode, SysNoticeSearchVO vo);

    void delete(String operatorCode, String code);

    void deleteAll(String operatorCode, String type);

    long findUnreadCountBy(String operatorCode);
}
