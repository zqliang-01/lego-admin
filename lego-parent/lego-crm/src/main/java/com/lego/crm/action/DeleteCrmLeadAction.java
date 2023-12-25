package com.lego.crm.action;

import com.lego.core.action.DeleteAction;
import com.lego.crm.dao.ICrmLeadDao;
import com.lego.crm.entity.CrmLead;

public class DeleteCrmLeadAction extends DeleteAction<CrmLead, ICrmLeadDao> {

    public DeleteCrmLeadAction(String operatorCode, String code) {
        super("crm:lead", operatorCode, code);
    }
}
