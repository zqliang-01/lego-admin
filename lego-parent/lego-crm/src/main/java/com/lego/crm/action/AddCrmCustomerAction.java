package com.lego.crm.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.crm.dao.ICrmCustomerDao;
import com.lego.crm.entity.CrmCustomer;
import com.lego.crm.entity.CrmDictionary;
import com.lego.crm.vo.CrmCustomerCreateVO;

public class AddCrmCustomerAction extends AddAction<CrmCustomer, ICrmCustomerDao> {

    private CrmCustomerCreateVO vo;

    public AddCrmCustomerAction(String operatorCode, CrmCustomerCreateVO vo) {
        super("crm_customer", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，客户新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，客户新增失败！");
    }

    @Override
    protected CrmCustomer createTargetEntity() {
        CrmCustomer entity = new CrmCustomer(vo.getCode(), vo.getName());
        entity.setMobile(vo.getMobile());
        entity.setWebsite(vo.getWebsite());
        entity.setEmail(vo.getEmail());
        entity.setType(findByCode(CrmDictionary.class, vo.getType()));
        return entity;
    }

}
