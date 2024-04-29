package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.dto.TypeInfo;
import com.lego.system.dao.ISysCodeGeneratorDao;
import com.lego.system.entity.SysCodeGenerator;
import com.lego.system.vo.SysCodeGeneratorModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysCodeGeneratorAction extends ModifyAction<SysCodeGenerator, ISysCodeGeneratorDao> {

    private SysCodeGeneratorModifyVO vo;

    public ModifySysCodeGeneratorAction(String operatorCode, SysCodeGeneratorModifyVO vo) {
        super(SysPermissionCode.manage, operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void doModify(SysCodeGenerator entity) {
        entity.setName(vo.getName());
        entity.setDatePattern(vo.getDatePattern());
        entity.setPrefix(vo.getPrefix());
        entity.setSerialLength(vo.getSerialLength());
    }

    public TypeInfo getTypeInfo() {
        return new TypeInfo(targetEntity.getCode(), targetEntity.getName());
    }
}
