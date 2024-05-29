package com.lego.doc.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.doc.dto.DocFileInfo;
import com.lego.doc.entity.DocFile;
import org.springframework.stereotype.Component;

@Component
public class DocFileAssembler extends EntityAssembler<DocFileInfo, DocFile> {

    @Override
    protected DocFileInfo doCreate(DocFile entity) {
        DocFileInfo info = new DocFileInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setSize(entity.getSize());
        info.setPath(entity.getPath());
        info.setEnable(entity.isEnable());
        info.setType(createTypeInfo(entity.getType()));
        info.setLocation(createTypeInfo(entity.getLocation()));
        return info;
    }
}
