package com.lego.system.service;

import com.lego.core.dto.LegoPage;
import com.lego.system.dto.SysOperationLogInfo;
import com.lego.system.vo.SysOperationLogSearchVO;

import java.util.List;

public interface ISysOperationLogService {

    List<SysOperationLogInfo> findBy(String loginCode, String entityCode, String permissionCode);

    LegoPage<SysOperationLogInfo> findBy(SysOperationLogSearchVO vo);
}
