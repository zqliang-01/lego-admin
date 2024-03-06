package com.lego.crm.assembler;

import com.lego.core.assembler.BusAssembler;
import com.lego.crm.dto.CrmContractInfo;
import com.lego.crm.entity.CrmContract;
import org.springframework.stereotype.Component;

@Component
public class CrmContractAssembler extends BusAssembler<CrmContractInfo, CrmContract> {

    @Override
    protected CrmContractInfo doCreate(CrmContract entity) {
        CrmContractInfo info = new CrmContractInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setStartTime(entity.getStartTime());
        info.setEndTime(entity.getEndTime());
        info.setOwnerCode(createEmployee(entity.getOwnerCode()));
        info.setAmount(entity.getAmount());
        info.setLead(createTypeInfo(entity.getLead()));
        info.setCustomer(createTypeInfo(entity.getCustomer()));
        info.setType(createTypeInfo(entity.getType()));
        return info;
    }
}
