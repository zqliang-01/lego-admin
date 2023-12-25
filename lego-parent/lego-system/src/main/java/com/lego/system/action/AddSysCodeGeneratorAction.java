package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.dto.TypeInfo;
import com.lego.system.dao.ISysCodeGeneratorDao;
import com.lego.system.entity.SysCodeGenerator;
import com.lego.system.vo.SysCodeGeneratorCreateVO;
import com.lego.system.vo.SysPermissionCode;

public class AddSysCodeGeneratorAction extends AddAction<SysCodeGenerator, ISysCodeGeneratorDao> {

	private SysCodeGeneratorCreateVO vo;

	public AddSysCodeGeneratorAction(String operatorCode, SysCodeGeneratorCreateVO vo) {
		super(SysPermissionCode.manage, operatorCode);
		this.vo = vo;
	}

	@Override
	protected SysCodeGenerator createTargetEntity() {
		SysCodeGenerator generator = new SysCodeGenerator(vo.getName());
		generator.setDatePattern(vo.getDatePattern());
		generator.setPrefix(vo.getPrefix());
		generator.setSerialLength(vo.getSerialLength());
		return generator;
	}

	public TypeInfo getTypeInfo() {
		return new TypeInfo(targetEntity.getCode(), targetEntity.getName());
	}
}
