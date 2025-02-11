package com.lego.system.service;

import com.lego.core.dto.LegoPage;
import com.lego.system.dto.SysNoticeTemplateInfo;
import com.lego.system.vo.SysNoticeTemplateCreateVO;
import com.lego.system.vo.SysNoticeTemplateModifyVO;
import com.lego.system.vo.SysNoticeTemplateSearchVO;

public interface ISysNoticeTemplateService {

    LegoPage<SysNoticeTemplateInfo> findBy(String operatorCode, SysNoticeTemplateSearchVO vo);

    void add(String operatorCode, SysNoticeTemplateCreateVO vo);

    void modify(String operatorCode, SysNoticeTemplateModifyVO vo);

    void publish(String operatorCode, String code);
}
