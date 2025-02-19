package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.core.enums.ActionType;
import com.lego.system.dto.SysOperationLogInfo;
import com.lego.system.entity.SysOperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysOperationLogAssembler extends EntityAssembler<SysOperationLogInfo, SysOperationLog> {

    @Autowired
    private SysEmployeeAssembler employeeAssembler;

    @Override
    protected SysOperationLogInfo doCreate(SysOperationLog entity) {
        SysOperationLogInfo info = new SysOperationLogInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setEntityCode(entity.getEntityCode());
        info.setEntityName(entity.getEntityName());
        info.setOperator(employeeAssembler.createSimple(entity.getOperator()));
        info.setAction(ActionType.getName(entity.getActionType()));
        info.setDescription(entity.getDescription());
        info.setCreateTime(entity.getCreateTime());
        info.setPermission(createTypeInfo(entity.getPermission()));
        return info;
    }

}
