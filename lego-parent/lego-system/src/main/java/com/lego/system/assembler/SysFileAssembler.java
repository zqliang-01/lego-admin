package com.lego.system.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.system.dto.SysFileInfo;
import com.lego.system.entity.SysFile;

@Component
public class SysFileAssembler extends BaseAssembler<SysFileInfo, SysFile> {

	@Override
	protected SysFileInfo doCreate(SysFile entity) {
		SysFileInfo info = new SysFileInfo();
		info.setCode(entity.getCode());
		info.setCreateTime(entity.getCreateTime());
		info.setCreator(createTypeInfo(entity.getCreator()));
		info.setType(createTypeInfo(entity.getType()));
		info.setName(entity.getName());
		info.setSize(entity.getSize());
		return info;
	}

}
