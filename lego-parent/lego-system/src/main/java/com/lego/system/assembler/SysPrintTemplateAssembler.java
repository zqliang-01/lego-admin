package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysPrintTemplateInfo;
import com.lego.system.entity.SysPrintTemplate;
import org.springframework.stereotype.Component;

@Component
public class SysPrintTemplateAssembler extends EntityAssembler<SysPrintTemplateInfo, SysPrintTemplate> {

    @Override
    protected SysPrintTemplateInfo doCreate(SysPrintTemplate entity) {
        SysPrintTemplateInfo info = new SysPrintTemplateInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setContent(entity.getContent());
        info.setCreator(createTypeInfo(entity.getCreator()));
        info.setForm(createTypeInfo(entity.getForm()));
        return info;
    }
}
