package com.lego.doc.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.core.util.EntityUtil;
import com.lego.doc.dto.DocBookInfo;
import com.lego.doc.entity.DocBook;
import org.springframework.stereotype.Component;

@Component
public class DocBookAssembler extends EntityAssembler<DocBookInfo, DocBook> {

    @Override
    protected DocBookInfo doCreate(DocBook entity) {
        DocBookInfo info = new DocBookInfo();
        info.setOpen(entity.isOpen());
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setEnable(entity.isEnable());
        info.setCreateTime(entity.getCreateTime());
        info.setUpdateTime(entity.getUpdateTime());
        info.setDescription(entity.getDescription());
        info.setCover(EntityUtil.getCode(entity.getCover()));
        info.setCreator(createEmployee(entity.getCreatorCode()));
        return info;
    }
}
