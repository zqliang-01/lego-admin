package com.lego.system.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BaseAssembler;
import com.lego.system.dto.SysColumnSortInfo;
import com.lego.system.entity.SysColumnSort;

@Component
public class SysColumnSortAssembler extends BaseAssembler<SysColumnSortInfo, SysColumnSort> {

	@Override
	protected SysColumnSortInfo doCreate(SysColumnSort entity) {
		SysColumnSortInfo info = new SysColumnSortInfo();
		info.setCode(entity.getCode());
		info.setName(entity.getName());
		info.setVisible(entity.isVisible());
		info.setSn(entity.getSn());
		info.setWidth(entity.getWidth());
		return info;
	}

}
