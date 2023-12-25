package com.lego.system.action;

import java.util.List;
import java.util.Optional;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.system.dao.ISysAppSortDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.entity.SysAppSort;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysPermission;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysAppSortAction extends MaintainAction {

	private List<String> permissionCodes;

	private ISysAppSortDao sortDao = getDao(ISysAppSortDao.class);
	private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);
	private ISysPermissionDao permissionDao = getDao(ISysPermissionDao.class);

	public ModifySysAppSortAction(String operatorCode, List<String> permissionCodes) {
		super(SysPermissionCode.manage, operatorCode);
		this.permissionCodes = permissionCodes;
	}

	@Override
	protected void doRun() {
		List<SysAppSort> appSorts = sortDao.findByEmployee(operatorCode);
		if (permissionCodes.isEmpty()) {
			sortDao.deleteAll(appSorts);
			return;
		}
		modifyAppSort(appSorts);
		deleteAppSort(appSorts);
		this.description = "更新模型顺序";
	}

	private void modifyAppSort(List<SysAppSort> appSorts) {
		SysEmployee employee = employeeDao.findByCode(operatorCode);
		for (int i = 0; i < permissionCodes.size(); ++i) {
			String permissionCode = permissionCodes.get(i);
			Optional<SysAppSort> optional = appSorts.stream().filter(s -> s.getPermission().getCode().equals(permissionCode)).findFirst();
			SysAppSort appSort = optional.orElse(null);
			if (appSort == null) {
				SysPermission permission = permissionDao.findByCode(permissionCode);
				appSort = new SysAppSort(i, permission, employee);
			}
			appSort.setSn(i);
			sortDao.save(appSort);
		}
	}

	private void deleteAppSort(List<SysAppSort> appSorts) {
		for (SysAppSort appSort : appSorts) {
			Optional<String> optional = permissionCodes.stream().filter(permissionCode -> appSort.getPermission().getCode().equals(permissionCode)).findFirst();
			if (!optional.isPresent()) {
				sortDao.delete(appSort);
			}
		}
	}

	@Override
	protected ActionType getActionType() {
		return ActionType.MODIFY;
	}

}
