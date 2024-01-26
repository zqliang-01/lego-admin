package com.lego.crm.action;

import com.lego.core.action.DeleteAction;
import com.lego.crm.dao.ICrmContractDao;
import com.lego.crm.entity.CrmContract;

public class DeleteCrmContractAction extends DeleteAction<CrmContract, ICrmContractDao> {

    public DeleteCrmContractAction(String operatorCode, String code) {
        super("crm_contract", operatorCode, code);
    }
}
