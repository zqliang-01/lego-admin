package com.lego.system.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.system.dto.SysGenTableInfo;
import com.lego.system.entity.SysGenTable;

@Component
public class SysGenTableAssembler extends BaseAssembler<SysGenTableInfo, SysGenTable> {

	@Override
	protected SysGenTableInfo doCreate(SysGenTable entity) {
		SysGenTableInfo info = new SysGenTableInfo();
		info.setCode(entity.getCode());
		info.setName(entity.getName());
		info.setPath(entity.getPath());
		info.setComment(entity.getComment());
		info.setAppCode(entity.getAppCode());
		info.setUrlName(entity.getUrlName());
		info.setClassName(entity.getClassName());
		info.setFieldName(entity.getFieldName());
		info.setPackageName(entity.getPackageName());
		info.setPermissionCode(entity.getPermissionCode());
		info.setCreator(createTypeInfo(entity.getCreator()));
		return info;
	}

}
