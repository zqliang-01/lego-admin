package com.lego.system.action;

import java.util.List;
import java.util.stream.Collectors;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysRole;
import com.lego.system.vo.SysEmployeeRoleModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysEmployeeRoleAction extends ModifyAction<SysEmployee, ISysEmployeeDao> {

	private SysEmployeeRoleModifyVO vo;

	private ISysRoleDao roleDao = getDao(ISysRoleDao.class);

	public ModifySysEmployeeRoleAction(String operator, SysEmployeeRoleModifyVO vo) {
		super(SysPermissionCode.manageUser, operator, vo.getCode());
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCodes()), "角色不能为空！");
	}

	@Override
	protected void doModify(SysEmployee entity) {
		List<SysRole> roles = roleDao.findByCodes(vo.getCodes());
		if (!vo.isCleanable()) {
			List<SysRole> notExistsRoles = entity.getRoles().stream().filter(role -> !roles.contains(role)).collect(Collectors.toList());
			roles.addAll(notExistsRoles);
		}
		entity.setRoles(roles);
	}
}