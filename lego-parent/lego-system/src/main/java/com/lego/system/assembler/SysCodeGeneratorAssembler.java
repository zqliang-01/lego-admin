package com.lego.system.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.system.dto.SysCodeGeneratorInfo;
import com.lego.system.entity.SysCodeGenerator;

@Component
public class SysCodeGeneratorAssembler extends BaseAssembler<SysCodeGeneratorInfo, SysCodeGenerator> {

	@Override
	protected SysCodeGeneratorInfo doCreate(SysCodeGenerator entity) {
		SysCodeGeneratorInfo info = new SysCodeGeneratorInfo();
		info.setCode(entity.getCode());
		info.setName(entity.getName());
		info.setPrefix(entity.getPrefix());
		info.setSerialLength(entity.getSerialLength());
		info.setDatePattern(entity.getDatePattern());
		return info;
	}

}
