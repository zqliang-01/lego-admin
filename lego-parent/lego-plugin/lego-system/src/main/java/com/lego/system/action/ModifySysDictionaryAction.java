package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.system.dao.ISysDictionaryDao;
import com.lego.system.entity.SysDictionary;
import com.lego.system.vo.SysDictionaryVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysDictionaryAction extends ModifyAction<SysDictionary, ISysDictionaryDao> {

    private SysDictionaryVO vo;

    public ModifySysDictionaryAction(String operatorCode, SysDictionaryVO vo) {
        super(SysPermissionCode.manage, operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void doModify(SysDictionary entity) {
        entity.setName(vo.getName());
        entity.setSerialNumber(vo.getSn());
        entity.setEnable(vo.isEnable());
    }
}
