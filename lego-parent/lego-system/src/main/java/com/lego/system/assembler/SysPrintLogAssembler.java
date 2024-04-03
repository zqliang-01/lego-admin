package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysPrintLogInfo;
import com.lego.system.entity.SysPrintLog;
import org.springframework.stereotype.Component;

@Component
public class SysPrintLogAssembler extends EntityAssembler<SysPrintLogInfo, SysPrintLog> {

    @Override
    protected SysPrintLogInfo doCreate(SysPrintLog entity) {
        SysPrintLogInfo info = new SysPrintLogInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setContent(entity.getContent());
        info.setCreateTime(entity.getCreateTime());
        info.setTemplate(createTypeInfo(entity.getTemplate()));
        info.setCreator(createTypeInfo(entity.getCreator()));
        return info;
    }
}
