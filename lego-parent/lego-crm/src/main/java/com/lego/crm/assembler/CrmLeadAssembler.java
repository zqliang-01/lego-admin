package com.lego.crm.assembler;

import com.lego.core.assembler.BusAssembler;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.crm.dto.CrmLeadInfo;
import com.lego.crm.entity.CrmLead;
import org.springframework.stereotype.Component;

@Component
public class CrmLeadAssembler extends BusAssembler<CrmLeadInfo, CrmLead> {

    @Override
    protected CrmLeadInfo doCreate(CrmLead entity) {
        CrmLeadInfo info = new CrmLeadInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setMobile(entity.getMobile());
        info.setAmount(entity.getAmount());
        info.setBrithday(entity.getBrithday());
        info.setAddress(entity.getAddress());
        info.setAge(entity.getAge());
        info.setSize(entity.getSize());
        info.setStatus(entity.isStatus());
        info.setEmail(entity.getEmail());
        info.setEmployee(create(CustomFieldTypeEnum.EMPLOYEE, entity.getEmployee()));
        info.setDept(create(CustomFieldTypeEnum.DEPT, entity.getDept()));
        info.setSource(createTypeInfo(entity.getSource()));
        info.setCustomer(createTypeInfo(entity.getCustomer()));
        return info;
    }
}
