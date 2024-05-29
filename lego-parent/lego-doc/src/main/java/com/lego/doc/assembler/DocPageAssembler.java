package com.lego.doc.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.doc.dto.DocPageInfo;
import com.lego.doc.entity.DocPage;
import org.springframework.stereotype.Component;

@Component
public class DocPageAssembler extends EntityAssembler<DocPageInfo, DocPage> {

    @Override
    protected DocPageInfo doCreate(DocPage entity) {
        DocPageInfo info = new DocPageInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setEnable(entity.isEnable());
        info.setContent(entity.getContent());
        info.setCreateTime(entity.getCreateTime());
        info.setBook(createTypeInfo(entity.getBook()));
        info.setCreator(createEmployee(entity.getCreatorCode()));
        return info;
    }
}
