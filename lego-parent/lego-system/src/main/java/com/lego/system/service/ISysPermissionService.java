package com.lego.system.service;

import java.util.List;

import com.lego.system.dto.SysAppInfo;
import com.lego.system.dto.SysPermissionInfo;
import com.lego.system.vo.SysPermissionCreateVO;

public interface ISysPermissionService {

	void add(String employeeCode, SysPermissionCreateVO vo);

	List<SysPermissionInfo> findAll();

	List<SysPermissionInfo> findByEmployee(String employeeCode);

	List<SysPermissionInfo> findAllMenu();

	List<SysAppInfo> findAllApp();

	List<SysAppInfo> findAppBy(String employeeCode);

	List<String> findSortAppBy(String employeeCode);

	List<String> findCodeBy(String roleCode);

	int findMaxSn(String appCode);
}
