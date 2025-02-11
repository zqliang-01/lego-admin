package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysPrintTemplateDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysPrintTemplate;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysPrintTemplateCreateVO;

public class AddSysPrintTemplateAction extends AddAction<SysPrintTemplate, ISysPrintTemplateDao> {

    private SysPrintTemplateCreateVO vo;

    public AddSysPrintTemplateAction(String operatorCode, SysPrintTemplateCreateVO vo) {
        super(SysPermissionCode.managePrintTemplate, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "模板名称不能为空，打印模板新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getForm()), "模板关联表单不能为空，打印模板新增失败！");
    }

    @Override
    protected SysPrintTemplate createTargetEntity() {
        SysCustomForm form = findByCode(SysCustomForm.class, vo.getForm());
        SysEmployee creator = findByCode(SysEmployee.class, operatorCode);

        SysPrintTemplate entity = new SysPrintTemplate(vo.getName(), form, creator);
        entity.setContent(vo.getContent());
        return entity;
    }

}
