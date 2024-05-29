package com.lego.doc.entity;

import com.lego.core.util.EntityUtil;
import com.lego.doc.entity.simpletype.DocNodeType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Map;

@Entity
@Getter
@Setter
@DiscriminatorValue("DocNodeFile")
public class DocNodeFile extends DocNode {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "file_id")
    private DocFile file;

    protected DocNodeFile() {
    }

    public DocNodeFile(String creatorCode, DocFile file, DocBook book, DocNodeType type) {
        super(file.getName(), creatorCode, book, type);
        this.file = file;
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        super.doBuildReadableSnapshot(attributes);
        attributes.put("文件", EntityUtil.toString(file));
    }
}
