package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.core.util.StringUtil;
import com.lego.system.dto.SysMessageInfo;
import com.lego.system.entity.SysMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SysMessageAssembler extends EntityAssembler<SysMessageInfo, SysMessage> {

    @Override
    protected SysMessageInfo doCreate(SysMessage entity) {
        SysMessageInfo info = new SysMessageInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setContent(StringUtil.replaceBrace(entity.getContent(), getParams(entity)));
        info.setReaded(entity.isReaded());
        info.setEntityCode(entity.getEntityCode());
        info.setCreateTime(entity.getCreateTime());
        info.setReadTime(entity.getReadTime());
        info.setCreator(createTypeInfo(entity.getCreator()));
        info.setRecipient(createTypeInfo(entity.getRecipient()));
        info.setType(createTypeInfo(entity.getType()));
        return info;
    }

    private Map<String, String> getParams(SysMessage entity) {
        Map<String, String> params = new HashMap<>();
        params.put("creator", entity.getCreator().getName());
        params.put("title", entity.getName());
        return params;
    }
}
