package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysPrintLogDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysPermission;
import com.lego.system.entity.SysPrintLog;
import com.lego.system.entity.SysPrintTemplate;
import com.lego.system.vo.SysPrintLogCreateVO;

public class AddSysPrintLogAction extends AddAction<SysPrintLog, ISysPrintLogDao> {

    private SysPrintLogCreateVO vo;

    public AddSysPrintLogAction(String operatorCode, SysPrintLogCreateVO vo) {
        super(vo.getPermissionCode(), operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getContent()), "打印内容不能为空，生成打印记录失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getTemplateCode()), "打印模板不能为空，生成打印记录失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getPermissionCode()), "受理功能不能为空，生成打印记录失败！");
    }

    @Override
    protected SysPrintLog createTargetEntity() {
        SysEmployee creator = findByCode(SysEmployee.class, operatorCode);
        SysPermission permission = findByCode(SysPermission.class, vo.getPermissionCode());
        SysPrintTemplate template = findByCode(SysPrintTemplate.class, vo.getTemplateCode());

        SysPrintLog log = new SysPrintLog(template, permission, creator);
        log.setContent(vo.getContent());
        log.setEntityCode(vo.getEntityCode());
        return log;
    }
}
