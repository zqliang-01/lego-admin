package com.lego.crm.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BusiAssembler;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.crm.dto.CrmContractInfo;
import com.lego.crm.entity.CrmContract;

@Component
public class CrmContractAssembler extends BusiAssembler<CrmContractInfo, CrmContract> {

    @Override
    protected CrmContractInfo doCreate(CrmContract entity) {
        CrmContractInfo info = new CrmContractInfo();
    	info.setCode(entity.getCode());
    	info.setName(entity.getName());
    	info.setStartTime(entity.getStartTime());
    	info.setEndTime(entity.getEndTime());
    	info.setOwnerCode(create(CustomFieldTypeEnum.EMPLOYEE, entity.getOwnerCode()));
    	info.setAmount(entity.getAmount());
    	info.setLead(createTypeInfo(entity.getLead()));
    	info.setCustomer(createTypeInfo(entity.getCustomer()));
    	info.setType(createTypeInfo(entity.getType()));
        return info;
    }
}
