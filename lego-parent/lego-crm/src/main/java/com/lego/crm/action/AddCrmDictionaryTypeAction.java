package com.lego.crm.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.DictionaryTypeVO;
import com.lego.crm.dao.ICrmDictionaryTypeDao;
import com.lego.crm.entity.simpletype.CrmDictionaryType;

public class AddCrmDictionaryTypeAction extends AddAction<CrmDictionaryType, ICrmDictionaryTypeDao> {

    private DictionaryTypeVO vo;

    public AddCrmDictionaryTypeAction(String operatorCode, DictionaryTypeVO vo) {
        super("crm", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "字典类型名称不能为空！");
    }

    @Override
    protected CrmDictionaryType createTargetEntity() {
        return new CrmDictionaryType(vo.getName(), vo.getSn());
    }
}
