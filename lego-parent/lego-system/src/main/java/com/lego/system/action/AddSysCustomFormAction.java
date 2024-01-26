package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dao.ISysGenTableDao;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.vo.SysCustomFormCreateVO;
import com.lego.system.vo.SysPermissionCode;

public class AddSysCustomFormAction extends AddAction<SysCustomForm, ISysCustomFormDao> {

    private SysCustomFormCreateVO vo;
    private ISysGenTableDao tableDao = getDao(ISysGenTableDao.class);
    private ISysPermissionDao permissionDao = getDao(ISysPermissionDao.class);

    public AddSysCustomFormAction(String operatorCode, SysCustomFormCreateVO vo) {
        super(SysPermissionCode.manageCustomForm, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "表单编码不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "表单名称不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getTable()), "数据表不能为空！");

        SysCustomForm form = entityDao.findByTableCode(vo.getTable());
        if (form != null) {
            throw new BusinessException("数据表[{0}]已经关联表单[{1}]，无法再次关联！", EntityUtil.getName(form.getTable()), form.getName());
        }
    }

    @Override
    protected SysCustomForm createTargetEntity() {
        SysCustomForm form = new SysCustomForm(vo.getCode(), vo.getName());
        form.setEnable(vo.isEnable());
        form.setTable(tableDao.findByCode(vo.getTable()));
        form.setQueryApiUrl(vo.getQueryApiUrl());
        form.setDetailApiUrl(vo.getDetailApiUrl());
        form.setAddApiUrl(vo.getAddApiUrl());
        form.setUpdateApiUrl(vo.getUpdateApiUrl());
        form.setDeleteApiUrl(vo.getDeleteApiUrl());
        form.setExportAllApiUrl(vo.getExportAllApiUrl());
        form.setExportApiUrl(vo.getExportApiUrl());
        return form;
    }
}
