package com.lego.crm.action;

import com.lego.core.action.DeleteAction;
import com.lego.crm.dao.ICrmCustomerDao;
import com.lego.crm.entity.CrmCustomer;

public class DeleteCrmCustomerAction extends DeleteAction<CrmCustomer, ICrmCustomerDao> {

    public DeleteCrmCustomerAction(String operatorCode, String code) {
        super("crm_customer", operatorCode, code);
    }
}