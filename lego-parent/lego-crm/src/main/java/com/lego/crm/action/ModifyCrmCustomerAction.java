package com.lego.crm.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.crm.dao.ICrmCustomerDao;
import com.lego.crm.entity.CrmCustomer;
import com.lego.crm.vo.CrmCustomerModifyVO;
import com.lego.crm.entity.CrmDictionary;

public class ModifyCrmCustomerAction extends ModifyAction<CrmCustomer, ICrmCustomerDao> {

    private CrmCustomerModifyVO vo;

    public ModifyCrmCustomerAction(String operatorCode, CrmCustomerModifyVO vo) {
        super("crm:customer", operatorCode, vo.getCode());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，客户修改失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，客户修改失败！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getMobile()), "手机号不能为空，客户修改失败！");
    }

    @Override
    protected void doModify(CrmCustomer entity) {
        entity.setName(vo.getName());
        entity.setMobile(vo.getMobile());
        entity.setWebsite(vo.getWebsite());
        entity.setEmail(vo.getEmail());
        entity.setType(findByCode(CrmDictionary.class, vo.getType()));
    }

}
