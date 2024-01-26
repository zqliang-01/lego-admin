package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.system.assembler.SysOperationLogAssembler;
import com.lego.system.dao.ISysOperationLogDao;
import com.lego.system.dto.SysOperationLogInfo;
import com.lego.system.entity.SysOperationLog;
import com.lego.system.service.ISysOperationLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOperationLogService extends BusService<ISysOperationLogDao, SysOperationLogAssembler> implements ISysOperationLogService {

    @Override
    public List<SysOperationLogInfo> findBy(String loginCode, String entityCode, String permissionCode) {
        List<SysOperationLog> logs = dao.findBy(loginCode, entityCode, permissionCode);
        return assembler.create(logs);
    }

}
