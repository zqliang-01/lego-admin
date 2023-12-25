package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysCustomFieldDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.entity.SysCustomField;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.vo.SysPermissionCode;

public class AddSysColumnSortAction extends AddAction<SysColumnSort, ISysColumnSortDao> {

	private int sn;
	private String fieldCode;

	private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);
	private ISysCustomFieldDao fieldDao = getDao(ISysCustomFieldDao.class);

	public AddSysColumnSortAction(String operatorCode, String fieldCode, int sn) {
		super(SysPermissionCode.manage, operatorCode);
		this.fieldCode = fieldCode;
		this.sn = sn;
	}

	@Override
	protected void preprocess() {
		BusinessException.check(StringUtil.isNotBlank(fieldCode), "表格排序创建失败，表单字段编码不能为空！");
	}

	@Override
	protected SysColumnSort createTargetEntity() {
		SysCustomField field = fieldDao.findByCode(fieldCode);
		SysColumnSort columnSort = new SysColumnSort(field);
		columnSort.setEmployee(employeeDao.findByCode(operatorCode));
		columnSort.setSn(sn);
		return columnSort;
	}

}
