package com.lego.system.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.core.assembler.IBaseAssembler;
import com.lego.system.dto.SysRoleInfo;
import com.lego.system.entity.SysRole;

@Component
public class SysRoleAssembler extends BaseAssembler<SysRoleInfo, SysRole> implements IBaseAssembler<SysRoleInfo, SysRole> {

	@Override
	protected SysRoleInfo doCreate(SysRole entity) {
		SysRoleInfo info = new SysRoleInfo();
		info.setCode(entity.getCode());
		info.setName(entity.getName());
		info.setCreateTime(entity.getCreateTime());
		return info;
	}
}
