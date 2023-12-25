package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysColumnSortModifyVO;

public class ModifySysColumnSortAction extends ModifyAction<SysColumnSort, ISysColumnSortDao> {

	private SysColumnSortModifyVO vo;

	public ModifySysColumnSortAction(String operatorCode, SysColumnSortModifyVO vo) {
		super(SysPermissionCode.manage, operatorCode, vo.getCode());
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
		this.noCheckDiff();
	}

	@Override
	protected void doModify(SysColumnSort entity) {
		entity.setVisible(vo.isVisible());
		entity.setWidth(vo.getWidth());
		if (vo.getSn() != null) {
			entity.setSn(vo.getSn());
		}
	}

	@Override
	protected void createLog() {
		if (StringUtil.isNotBlank(this.description)) {
			super.createLog();
		}
	}
}
