package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysGenTableDao;
import com.lego.system.entity.SysGenTable;
import com.lego.system.vo.SysGenTableModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysGenTableAction extends ModifyAction<SysGenTable, ISysGenTableDao> {

	private SysGenTableModifyVO vo;

	public ModifySysGenTableAction(String operatorCode, SysGenTableModifyVO vo) {
		super(SysPermissionCode.manageGenTable, operatorCode, vo.getCode());
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
		BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "表名不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getName()), "功能名称不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getPackageName()), "包名不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getAppCode()), "模块编码不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getClassName()), "类名不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getFieldName()), "属性名称不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getUrlName()), "资源名称不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getPermissionCode()), "权限编码不能为空！");
	}

	@Override
	protected void doModify(SysGenTable entity) {
		entity.setName(vo.getName());
		entity.setPath(vo.getPath());
		entity.setComment(vo.getComment());
		entity.setAppCode(vo.getAppCode());
		entity.setUrlName(vo.getUrlName());
		entity.setClassName(vo.getClassName());
		entity.setFieldName(vo.getFieldName());
		entity.setPackageName(vo.getPackageName());
		entity.setPermissionCode(vo.getPermissionCode());
	}

}
