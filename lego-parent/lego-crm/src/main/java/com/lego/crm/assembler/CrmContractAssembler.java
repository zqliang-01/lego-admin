package com.lego.crm.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BusAssembler;
import com.lego.crm.dto.CrmContractInfo;
import com.lego.crm.entity.CrmContract;

@Component
public class CrmContractAssembler extends BusAssembler<CrmContractInfo, CrmContract> {

    @Override
    protected CrmContractInfo doCreate(CrmContract entity) {
        CrmContractInfo info = new CrmContractInfo();
    	info.setCode(entity.getCode());
    	info.setName(entity.getName());
    	info.setStartTime(entity.getStartTime());
    	info.setEndTime(entity.getEndTime());
    	info.setOwner(createEmployee(entity.getOwner()));
    	info.setAmount(entity.getAmount());
    	info.setType(createDict(entity.getType()));
    	info.setAddress(entity.getAddress());
    	info.setLead(createTypeInfo(entity.getLead()));
    	info.setCustomer(createTypeInfo(entity.getCustomer()));
        return info;
    }
}