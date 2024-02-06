package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.dao.ISysGenTableDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.vo.SysCustomFormCreateVO;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;

public class AddSysCustomFormAction extends AddAction<SysCustomForm, ISysCustomFormDao> {

    private SysCustomFormCreateVO vo;
    private ISysGenTableDao tableDao = getDao(ISysGenTableDao.class);

    public AddSysCustomFormAction(String operatorCode, SysCustomFormCreateVO vo) {
        super(SysPermissionCode.manageCustomForm, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "表单编码不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "表单名称不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getTable()), "数据表不能为空！");

        List<SysCustomForm> forms = entityDao.findByName(vo.getName());
        BusinessException.check(forms.isEmpty(), "存在同名表单[{0}]，表单创建失败！", vo.getName());

        SysCustomForm form = entityDao.findByUnsureCode(vo.getCode());
        BusinessException.check(form == null, "已存在编码为[{0}]的表单信息，请更换编码后重新提交！", vo.getCode());
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
