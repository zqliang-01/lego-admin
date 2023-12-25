package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysEmployeePasswordModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysEmployeePasswordAction extends ModifyAction<SysEmployee, ISysEmployeeDao> {

	private SysEmployeePasswordModifyVO vo;

	public ModifySysEmployeePasswordAction(String operatorCode, SysEmployeePasswordModifyVO vo) {
		super(SysPermissionCode.manageUser, operatorCode, vo.getCode());
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "员工编码不能为空！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getOriginalPassword()), "员工原密码不能为空！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getPassword()), "员工原密码不能为空！");
    	BusinessException.check(targetEntity.checkPassword(vo.getOriginalPassword()), "员工原密码不正确，密码修改失败！");
	}

	@Override
	protected void doModify(SysEmployee entity) {
		entity.resetPassword(vo.getPassword());
	}
}