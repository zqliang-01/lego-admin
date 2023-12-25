package com.lego.system.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.core.data.ActionType;
import com.lego.system.dto.SysOperationLogInfo;
import com.lego.system.entity.SysOperationLog;

@Component
public class SysOperationLogAssembler extends BaseAssembler<SysOperationLogInfo, SysOperationLog> {

	@Override
	protected SysOperationLogInfo doCreate(SysOperationLog entity) {
		SysOperationLogInfo info = new SysOperationLogInfo();
		info.setCode(entity.getCode());
		info.setName(entity.getName());
		info.setOperator(createTypeInfo(entity.getOperator()));
		info.setAction(ActionType.getName(entity.getCode()));
		info.setDescription(entity.getDescription());
		info.setCreateTime(entity.getCreateTime());
		return info;
	}

}
