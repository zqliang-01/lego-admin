package com.lego.crm.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BusiAssembler;
import com.lego.crm.dto.CrmCustomerInfo;
import com.lego.crm.entity.CrmCustomer;

@Component
public class CrmCustomerAssembler extends BusiAssembler<CrmCustomerInfo, CrmCustomer> {

    @Override
    protected CrmCustomerInfo doCreate(CrmCustomer entity) {
        CrmCustomerInfo info = new CrmCustomerInfo();
    	info.setCode(entity.getCode());
    	info.setName(entity.getName());
    	info.setMobile(entity.getMobile());
    	info.setWebsite(entity.getWebsite());
    	info.setEmail(entity.getEmail());
    	info.setType(createTypeInfo(entity.getType()));
        return info;
    }
}
