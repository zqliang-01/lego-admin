package com.lego.system.service;

import java.util.List;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysEmployeeInfo;
import com.lego.system.dto.SysLoginInfo;
import com.lego.system.vo.SysEmployeeCreateVO;
import com.lego.system.vo.SysEmployeeCurrentModifyVO;
import com.lego.system.vo.SysEmployeeModifyVO;
import com.lego.system.vo.SysEmployeePasswordModifyVO;
import com.lego.system.vo.SysEmployeeRoleModifyVO;
import com.lego.system.vo.SysEmployeeSearchVO;

public interface ISysEmployeeService {

	LegoPage<SysEmployeeInfo> findBy(SysEmployeeSearchVO vo);

	void add(String operatorCode, SysEmployeeCreateVO vo);

	void modify(String operatorCode, SysEmployeeModifyVO vo);

	void modifyCurrent(String operatorCode, SysEmployeeCurrentModifyVO vo);

	void modifyRole(String operatorCode, SysEmployeeRoleModifyVO vo);

	void modifyPassword(String operatorCode, SysEmployeePasswordModifyVO vo);

	SysLoginInfo login(String code, String password);

	SysEmployeeInfo findDTOByCode(String code);

	TypeInfo findSimpleTypeBy(String code);

	void updatePassword(String employeeCode, String oldPassword, String newPassword);

	List<TypeInfo> findSimpleType();

	void resetPassword(String employeeCode, List<String> codes);

}
