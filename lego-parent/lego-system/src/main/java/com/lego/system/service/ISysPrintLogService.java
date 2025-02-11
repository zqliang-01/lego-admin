package com.lego.system.service;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericSearchVO;
import com.lego.system.dto.SysPrintLogInfo;
import com.lego.system.vo.SysPrintLogCreateVO;

import java.util.List;

public interface ISysPrintLogService {

    LegoPage<SysPrintLogInfo> findPageBy(GenericSearchVO vo);

    List<SysPrintLogInfo> findBy(String permissionCode, String entityCode);

    SysPrintLogInfo findBy(String code);

    void add(String operator, SysPrintLogCreateVO vo);
}
