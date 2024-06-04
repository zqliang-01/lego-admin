package com.lego.crm.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.DictionaryTypeVO;
import com.lego.crm.dao.ICrmDictionaryTypeDao;
import com.lego.crm.entity.simpletype.CrmDictionaryType;

public class ModifyCrmDictionaryTypeAction extends ModifyAction<CrmDictionaryType, ICrmDictionaryTypeDao> {

    private DictionaryTypeVO vo;

    public ModifyCrmDictionaryTypeAction(String operatorCode, DictionaryTypeVO vo) {
        super("crm", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "字典类型名称不能为空！");
    }

    @Override
    protected void doModify(CrmDictionaryType entity) {
        entity.setName(vo.getName());
        entity.setSerialNumber(vo.getSn());
    }
}
