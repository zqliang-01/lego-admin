package com.lego.system.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.system.dto.SysCustomFormInfo;
import com.lego.system.entity.SysCustomForm;

@Component
public class SysCustomFormAssembler extends BaseAssembler<SysCustomFormInfo, SysCustomForm> {

	@Override
	protected SysCustomFormInfo doCreate(SysCustomForm entity) {
		SysCustomFormInfo info = new SysCustomFormInfo();
		info.setCode(entity.getCode());
		info.setName(entity.getName());
		info.setTable(createTypeInfo(entity.getTable()));
		info.setPermission(createTypeInfo(entity.getPermission()));
		info.setCreateTime(entity.getCreateTime());
		return info;
	}

}
