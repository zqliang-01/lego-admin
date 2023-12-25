package com.lego.system.service;

import com.lego.core.dto.LegoPage;
import com.lego.system.dto.SysCustomFormInfo;
import com.lego.system.vo.SysCustomFormCreateVO;
import com.lego.system.vo.SysCustomFormFieldModifyVO;
import com.lego.system.vo.SysCustomFormModifyVO;
import com.lego.system.vo.SysCustomFormSearchVO;

public interface ISysCustomFormService {

	LegoPage<SysCustomFormInfo> findBy(SysCustomFormSearchVO vo);

	void modifyField(String operatorCode, SysCustomFormFieldModifyVO vo);

	void modify(String operatorCode, SysCustomFormModifyVO vo);

	void add(String employeeCode, SysCustomFormCreateVO vo);

	void delete(String operatorCode, String code);

	String findAppCodeBy(String code);

	String findCodeByPermission(String permissionCode);

}
