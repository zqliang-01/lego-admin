package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysNoticeTemplateInfo;
import com.lego.system.entity.SysNoticeTemplate;
import org.springframework.stereotype.Component;

@Component
public class SysNoticeTemplateAssembler extends EntityAssembler<SysNoticeTemplateInfo, SysNoticeTemplate> {

    @Override
    protected SysNoticeTemplateInfo doCreate(SysNoticeTemplate entity) {
        SysNoticeTemplateInfo info = new SysNoticeTemplateInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setContent(entity.getContent());
        info.setPublished(entity.isPublished());
        info.setPublishedTime(entity.getPublishedTime());
        info.setCreator(createTypeInfo(entity.getCreator()));
        info.setEmployees(createTypeInfo(entity.getEmployees()));
        info.setDepts(createTypeInfo(entity.getDepts()));
        return info;
    }
}
