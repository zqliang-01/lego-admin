package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysDictionaryInfo;
import com.lego.system.entity.SysDictionary;
import org.springframework.stereotype.Component;

@Component
public class SysDictionaryAssembler extends EntityAssembler<SysDictionaryInfo, SysDictionary> {

    @Override
    protected SysDictionaryInfo doCreate(SysDictionary entity) {
        SysDictionaryInfo info = new SysDictionaryInfo(entity.getCode(), entity.getName());
        info.setSn(entity.getSerialNumber());
        info.setEnable(entity.isEnable());
        return info;
    }
}
