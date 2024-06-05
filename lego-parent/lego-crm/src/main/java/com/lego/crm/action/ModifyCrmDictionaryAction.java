package com.lego.crm.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.vo.DictionaryVO;
import com.lego.crm.dao.ICrmDictionaryDao;
import com.lego.crm.entity.CrmDictionary;

public class ModifyCrmDictionaryAction extends ModifyAction<CrmDictionary, ICrmDictionaryDao> {

    private DictionaryVO vo;

    public ModifyCrmDictionaryAction(String operatorCode, DictionaryVO vo) {
        super("crm", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void doModify(CrmDictionary entity) {
        entity.setName(vo.getName());
        entity.setSerialNumber(vo.getSn());
        entity.setEnable(vo.isEnable());
    }
}
