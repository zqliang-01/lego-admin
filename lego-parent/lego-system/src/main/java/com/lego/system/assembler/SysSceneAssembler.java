package com.lego.system.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.system.dto.SysSceneInfo;
import com.lego.system.entity.SysScene;

@Component
public class SysSceneAssembler extends BaseAssembler<SysSceneInfo, SysScene> {

	@Override
	protected SysSceneInfo doCreate(SysScene entity) {
		SysSceneInfo info = new SysSceneInfo();
		info.setCode(entity.getCode());
		info.setName(entity.getName());
		info.setData(entity.getData());
		info.setFormCode(entity.getForm().getCode());
		info.setCurrent(entity.isCurrent());
		info.setEnable(entity.isVisible());
		return info;
	}

}
