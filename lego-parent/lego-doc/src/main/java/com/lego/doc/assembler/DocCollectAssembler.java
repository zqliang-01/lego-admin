package com.lego.doc.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.doc.dto.DocCollectInfo;
import com.lego.doc.entity.DocCollect;
import com.lego.doc.entity.DocNode;
import org.springframework.stereotype.Component;

@Component
public class DocCollectAssembler extends EntityAssembler<DocCollectInfo, DocCollect> {

    @Override
    protected DocCollectInfo doCreate(DocCollect entity) {
        DocCollectInfo info = new DocCollectInfo();
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setEnable(entity.isEnable());
        DocNode node = entity.getNode();
        info.setNode(createTypeInfo(node));
        info.setType(node.getType().getCode());
        info.setBook(createTypeInfo(node.getBook()));
        info.setCreateTime(entity.getCreateTime());
        return info;
    }
}
