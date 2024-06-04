package com.lego.crm.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.DictionaryVO;
import com.lego.crm.dao.ICrmDictionaryDao;
import com.lego.crm.dao.ICrmDictionaryTypeDao;
import com.lego.crm.entity.CrmDictionary;
import com.lego.crm.entity.simpletype.CrmDictionaryType;

public class AddCrmDictionaryAction extends AddAction<CrmDictionary, ICrmDictionaryDao> {

    private DictionaryVO vo;

    private ICrmDictionaryTypeDao typeDao = getDao(ICrmDictionaryTypeDao.class);

    public AddCrmDictionaryAction(String operatorCode, DictionaryVO vo) {
        super("crm", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getTypeCode()), "字典类型不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "字典名称不能为空！");
    }

    @Override
    protected CrmDictionary createTargetEntity() {
        CrmDictionaryType type = typeDao.findByCode(vo.getTypeCode());
        return new CrmDictionary(vo.getName(), type, vo.getSn());
    }
}
