package com.lego.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lego.core.common.Constants;
import com.lego.core.data.hibernate.impl.BusiService;
import com.lego.system.action.AddSysPermissionAction;
import com.lego.system.assembler.SysPermissionAssembler;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.dto.SysAppInfo;
import com.lego.system.dto.SysPermissionInfo;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysPermission;
import com.lego.system.service.ISysPermissionService;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysPermissionCreateVO;
import com.lego.system.vo.SysPermissionTypeCode;

@Service
public class SysPermissionService extends BusiService<ISysPermissionDao, SysPermissionAssembler> implements ISysPermissionService {

	@Autowired
	private ISysEmployeeDao employeeDao;

	@Override
	public void add(String employeeCode, SysPermissionCreateVO vo) {
		new AddSysPermissionAction(employeeCode, vo).run();
	}

	@Override
	public List<SysPermissionInfo> findAll() {
		List<SysPermission> permissions = dao.findAll();
		return assembler.createTree(permissions);
	}

	@Override
	public List<SysPermissionInfo> findByEmployee(String employeeCode) {
		SysEmployee employee = employeeDao.findByCode(employeeCode);
		if (employee.containRole(Constants.ADMIN_ROLE)) {
			return assembler.createTree(dao.findAll());
		}
		List<SysPermission> permissions = dao.findByEmployee(employeeCode);
		return assembler.createTree(permissions);
	}

	@Override
	public List<SysPermissionInfo> findAllMenu() {
		List<SysPermission> permissions = dao.findByType(SysPermissionTypeCode.APP, SysPermissionTypeCode.MENU);
		permissions = permissions.stream().filter(p -> !p.getCode().startsWith(SysPermissionCode.manage)).collect(Collectors.toList());
		return assembler.createTree(permissions);
	}

	@Override
	public List<SysAppInfo> findAllApp() {
		List<SysPermission> permissions = dao.findByType(SysPermissionTypeCode.APP);
		return assembler.createApp(permissions);
	}

	@Override
	public List<SysAppInfo> findAppBy(String employeeCode) {
		SysEmployee employee = employeeDao.findByCode(employeeCode);
		if (employee.containRole(Constants.ADMIN_ROLE)) {
			return findAllApp();
		}
		List<SysPermission> permissions = dao.findBy(employeeCode, SysPermissionTypeCode.APP);
		return assembler.createApp(permissions);
	}

	@Override
	public List<String> findSortAppBy(String employeeCode) {
		return dao.findSortCodeBy(employeeCode, SysPermissionTypeCode.APP);
	}

	@Override
	public List<String> findCodeBy(String roleCode) {
		if (Constants.ADMIN_ROLE.equals(roleCode)) {
			return dao.findAllCode();
		}
		return dao.findCodeByRole(roleCode);
	}

	@Override
	public int findMaxSn(String appCode) {
		return dao.findMaxSn(appCode);
	}

}
