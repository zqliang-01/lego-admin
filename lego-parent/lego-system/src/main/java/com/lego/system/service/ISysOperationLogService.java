package com.lego.system.service;

import java.util.List;

import com.lego.system.dto.SysOperationLogInfo;

public interface ISysOperationLogService {

	List<SysOperationLogInfo> findBy(String loginCode, String entityCode, String permissionCode);

}
