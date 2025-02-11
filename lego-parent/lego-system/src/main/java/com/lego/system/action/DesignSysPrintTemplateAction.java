package com.lego.system.action;

import com.lego.core.action.EntityAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysPrintTemplateDao;
import com.lego.system.entity.SysPrintTemplate;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysPrintTemplateModifyVO;

public class DesignSysPrintTemplateAction extends EntityAction<SysPrintTemplate, ISysPrintTemplateDao> {

    private SysPrintTemplateModifyVO vo;

    public DesignSysPrintTemplateAction(String operatorCode, SysPrintTemplateModifyVO vo) {
        super(SysPermissionCode.managePrintTemplate, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "模板编码不能为空，打印模板修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getContent()), "模板内容不能为空，打印模板修改失败！");
    }

    @Override
    protected void doRun() {
        SysPrintTemplate template = entityDao.findByCode(vo.getCode());
        template.setContent(vo.getContent());
        entityDao.save(template);
        setTargetEntity(template);
        this.description = "设计打印模板：" + EntityUtil.toString(template);
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

}
