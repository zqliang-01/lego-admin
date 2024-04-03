package com.lego.system.action;

import com.lego.core.action.DeleteAction;
import com.lego.system.dao.ISysPrintTemplateDao;
import com.lego.system.entity.SysPrintTemplate;
import com.lego.system.vo.SysPermissionCode;

public class DeleteSysPrintTemplateAction extends DeleteAction<SysPrintTemplate, ISysPrintTemplateDao> {

    public DeleteSysPrintTemplateAction(String operatorCode, String code) {
        super(SysPermissionCode.managePrintTemplate, operatorCode, code);
    }
}
