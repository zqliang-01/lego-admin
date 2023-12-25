package com.lego.system.service;

import java.util.List;

import com.lego.system.dto.SysColumnSortInfo;
import com.lego.system.vo.SysColumnSortModifyVO;

public interface ISysColumnSortService {

	List<SysColumnSortInfo> findBy(String permissionCode, String employeeCode);

	void updateBy(String permissionCode, String employeeCode, List<String> fieldCodes);

	void update(String employeeCode, List<SysColumnSortModifyVO> vos);

	void update(String employeeCode, SysColumnSortModifyVO vo);
}
