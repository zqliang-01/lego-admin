package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysDictionaryTypeDao;
import com.lego.system.entity.simpletype.SysDictionaryType;
import com.lego.system.vo.SysDictionaryTypeVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysDictionaryTypeAction extends ModifyAction<SysDictionaryType, ISysDictionaryTypeDao> {

    private SysDictionaryTypeVO vo;

    public ModifySysDictionaryTypeAction(String operatorCode, SysDictionaryTypeVO vo) {
        super(SysPermissionCode.manage, operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "字典类型名称不能为空！");
    }

    @Override
    protected void doModify(SysDictionaryType entity) {
        entity.setName(vo.getName());
        entity.setSerialNumber(vo.getSn());
    }
}
