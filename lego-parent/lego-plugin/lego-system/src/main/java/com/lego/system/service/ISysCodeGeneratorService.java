package com.lego.system.service;

import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysCodeGeneratorInfo;
import com.lego.system.vo.SysCodeGeneratorCreateVO;
import com.lego.system.vo.SysCodeGeneratorModifyVO;

public interface ISysCodeGeneratorService {

	SysCodeGeneratorInfo findByCode(String code);

	TypeInfo add(String operatorCode, SysCodeGeneratorCreateVO vo);

	TypeInfo update(String operatorCode, SysCodeGeneratorModifyVO vo);

	void delete(String operatorCode, String code);

	String generate(String code);
}
