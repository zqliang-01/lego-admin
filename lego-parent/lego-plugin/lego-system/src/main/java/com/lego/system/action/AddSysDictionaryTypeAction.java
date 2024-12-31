package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysDictionaryTypeDao;
import com.lego.system.entity.simpletype.SysDictionaryType;
import com.lego.system.vo.SysDictionaryTypeVO;
import com.lego.system.vo.SysPermissionCode;

public class AddSysDictionaryTypeAction extends AddAction<SysDictionaryType, ISysDictionaryTypeDao> {

    private SysDictionaryTypeVO vo;

    public AddSysDictionaryTypeAction(String operatorCode, SysDictionaryTypeVO vo) {
        super(SysPermissionCode.manage, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "字典类型名称不能为空！");
    }

    @Override
    protected SysDictionaryType createTargetEntity() {
        return new SysDictionaryType(vo.getName(), vo.getSn());
    }
}
