package com.lego.crm.assembler;

import org.springframework.stereotype.Component;

import com.lego.core.assembler.BusAssembler;
import com.lego.crm.dto.CrmLeadInfo;
import com.lego.crm.entity.CrmLead;

@Component
public class CrmLeadAssembler extends BusAssembler<CrmLeadInfo, CrmLead> {

    @Override
    protected CrmLeadInfo doCreate(CrmLead entity) {
        CrmLeadInfo info = new CrmLeadInfo();
    	info.setCode(entity.getCode());
    	info.setName(entity.getName());
    	info.setMobile(entity.getMobile());
    	info.setAmount(entity.getAmount());
    	info.setBirthday(entity.getBirthday());
    	info.setAddress(entity.getAddress());
    	info.setAge(entity.getAge());
    	info.setSize(entity.getSize());
    	info.setStatus(entity.isStatus());
    	info.setEmail(entity.getEmail());
    	info.setEmployee(createEmployee(entity.getEmployee()));
    	info.setDept(createDept(entity.getDept()));
    	info.setSource(createDict(entity.getSource()));
    	info.setCustomer(createTypeInfo(entity.getCustomer()));
        return info;
    }
}