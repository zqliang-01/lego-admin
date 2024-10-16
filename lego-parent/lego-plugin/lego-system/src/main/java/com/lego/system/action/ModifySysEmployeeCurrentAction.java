package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysFile;
import com.lego.system.vo.SysEmployeeCurrentModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysEmployeeCurrentAction extends ModifyAction<SysEmployee, ISysEmployeeDao> {

    private SysEmployeeCurrentModifyVO vo;

    public ModifySysEmployeeCurrentAction(String operator, SysEmployeeCurrentModifyVO vo) {
        super(SysPermissionCode.manageUser, operator, operator);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "员工姓名不能为空！");
    }

    @Override
    protected void doModify(SysEmployee entity) {
        entity.setName(vo.getName());
        entity.setImage(findByUnsureCode(SysFile.class, vo.getImageCode()));
    }
}