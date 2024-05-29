package com.lego.doc.assembler;

import com.lego.core.assembler.TreeAssembler;
import com.lego.core.util.EntityUtil;
import com.lego.doc.dto.DocNodeDetailInfo;
import com.lego.doc.dto.DocNodeInfo;
import com.lego.doc.entity.DocNode;
import com.lego.doc.entity.DocNodeFile;
import com.lego.doc.entity.DocNodePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocNodeAssembler extends TreeAssembler<DocNodeInfo, DocNode> {

    @Autowired
    private DocFileAssembler fileAssembler;

    @Override
    protected DocNodeInfo doCreate(DocNode entity) {
        DocNodeInfo info = new DocNodeInfo();
        setAttribute(info, entity);
        return info;
    }

    private void setAttribute(DocNodeInfo info, DocNode entity) {
        info.setCode(entity.getCode());
        info.setName(entity.getName());
        info.setDescription(entity.getDescription());
        info.setType(entity.getType().getCode());
        info.setParentCode(EntityUtil.getCode(entity.getParent()));
        if (entity instanceof DocNodePage) {
            DocNodePage nodePage = (DocNodePage) entity;
            info.setPage(createTypeInfo(nodePage.getPage()));
        }
    }

    public DocNodeDetailInfo createDetail(DocNode entity) {
        DocNodeDetailInfo info = new DocNodeDetailInfo();
        setAttribute(info, entity);
        info.setCreateTime(entity.getCreateTime());
        info.setCreator(createEmployee(entity.getCreatorCode()));
        if (entity instanceof DocNodeFile) {
            DocNodeFile nodeFile = (DocNodeFile) entity;
            info.setFile(fileAssembler.create(nodeFile.getFile()));
        }
        return info;
    }

    public List<DocNodeDetailInfo> createDetail(List<DocNode> nodes) {
        List<DocNodeDetailInfo> infos = new ArrayList<>();
        for (DocNode node : nodes) {
            infos.add(createDetail(node));
        }
        return infos;
    }
}
