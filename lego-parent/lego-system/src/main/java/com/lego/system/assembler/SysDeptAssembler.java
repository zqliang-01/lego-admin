package com.lego.system.assembler;

import com.lego.core.assembler.TreeAssembler;
import com.lego.system.dto.SysDeptInfo;
import com.lego.system.entity.SysDept;
import org.springframework.stereotype.Component;

@Component
public class SysDeptAssembler extends TreeAssembler<SysDeptInfo, SysDept> {

    @Override
    protected SysDeptInfo doCreate(SysDept entity) {
        SysDeptInfo info = new SysDeptInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setEnable(entity.isEnable());
        info.setLeader(createTypeInfo(entity.getLeader()));
        info.setSerialNumber(entity.getSerialNumber());
        return info;
    }

}
