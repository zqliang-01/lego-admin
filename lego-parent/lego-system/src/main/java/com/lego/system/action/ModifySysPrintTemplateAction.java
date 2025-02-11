package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysPrintTemplateDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysPrintTemplate;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysPrintTemplateModifyVO;

public class ModifySysPrintTemplateAction extends ModifyAction<SysPrintTemplate, ISysPrintTemplateDao> {

    private SysPrintTemplateModifyVO vo;

    public ModifySysPrintTemplateAction(String operatorCode, SysPrintTemplateModifyVO vo) {
        super(SysPermissionCode.managePrintTemplate, operatorCode, vo.getCode());
        this.setCheckDiff(vo.isCheckDiff());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "模板编码不能为空，打印模板修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "模板名称不能为空，打印模板修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getForm()), "模板关联表单不能为空，打印模板修改失败！");
    }

    @Override
    protected void doModify(SysPrintTemplate entity) {
        entity.setName(vo.getName());
        entity.setForm(findByCode(SysCustomForm.class, vo.getForm()));
    }

}
