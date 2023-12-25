package com.lego.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BusiService;
import com.lego.system.assembler.SysOperationLogAssembler;
import com.lego.system.dao.ISysOperationLogDao;
import com.lego.system.dto.SysOperationLogInfo;
import com.lego.system.entity.SysOperationLog;
import com.lego.system.service.ISysOperationLogService;

@Service
public class SysOperationLogService extends BusiService<ISysOperationLogDao, SysOperationLogAssembler> implements ISysOperationLogService {

	@Override
	public List<SysOperationLogInfo> findBy(String loginCode, String entityCode, String permissionCode) {
		List<SysOperationLog> logs = dao.findBy(loginCode, entityCode, permissionCode);
		return assembler.create(logs);
	}

}
