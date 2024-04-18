package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysNoticeInfo;
import com.lego.system.entity.SysNotice;
import org.springframework.stereotype.Component;

@Component
public class SysNoticeAssembler extends EntityAssembler<SysNoticeInfo, SysNotice> {

    @Override
    protected SysNoticeInfo doCreate(SysNotice entity) {
        SysNoticeInfo info = new SysNoticeInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setContent(entity.getContent());
        info.setReaded(entity.isReaded());
        info.setCreateTime(entity.getCreateTime());
        info.setReadTime(entity.getReadTime());
        info.setCreator(createTypeInfo(entity.getCreator()));
        info.setRecipient(createTypeInfo(entity.getRecipient()));
        return info;
    }

}
