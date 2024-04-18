package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysNoticeTemplateDao;
import com.lego.system.entity.SysDept;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysNoticeTemplate;
import com.lego.system.vo.SysNoticeTemplateCreateVO;
import com.lego.system.vo.SysPermissionCode;

public class AddSysNoticeTemplateAction extends AddAction<SysNoticeTemplate, ISysNoticeTemplateDao> {

    private SysNoticeTemplateCreateVO vo;

    public AddSysNoticeTemplateAction(String operatorCode, SysNoticeTemplateCreateVO vo) {
        super(SysPermissionCode.manage, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "公告标题不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getContent()), "公告内容不能为空！");
    }

    @Override
    protected SysNoticeTemplate createTargetEntity() {
        SysEmployee creator = findByCode(SysEmployee.class, operatorCode);
        SysNoticeTemplate template = new SysNoticeTemplate(vo.getName(), vo.getContent(), creator);
        template.setEmployees(findByCodes(SysEmployee.class, vo.getEmployees()));
        template.setDepts(findByCodes(SysDept.class, vo.getDepts()));
        return template;
    }
}
