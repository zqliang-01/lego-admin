package com.lego.doc.entity;

import com.lego.core.util.EntityUtil;
import com.lego.core.vo.ReadableVO;
import com.lego.doc.entity.simpletype.DocNodeType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        super.doBuildReadableSnapshot(attributes);
        attributes.put("文件", EntityUtil.toString(file));
    }
}
