package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysDictionaryDao;
import com.lego.system.dao.ISysDictionaryTypeDao;
import com.lego.system.entity.SysDictionary;
import com.lego.system.entity.simpletype.SysDictionaryType;
import com.lego.system.vo.SysDictionaryVO;
import com.lego.system.vo.SysPermissionCode;

public class AddSysDictionaryAction extends AddAction<SysDictionary, ISysDictionaryDao> {

    private SysDictionaryVO vo;

    private ISysDictionaryTypeDao typeDao = getDao(ISysDictionaryTypeDao.class);

    public AddSysDictionaryAction(String operatorCode, SysDictionaryVO vo) {
        super(SysPermissionCode.manage, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getTypeCode()), "字典类型不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "字典名称不能为空！");
    }

    @Override
    protected SysDictionary createTargetEntity() {
        SysDictionaryType type = typeDao.findByCode(vo.getTypeCode());
        return new SysDictionary(vo.getName(), type, vo.getSn());
    }
}
