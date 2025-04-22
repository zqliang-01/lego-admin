package com.lego.doc.entity;

import com.lego.doc.entity.simpletype.DocNodeType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("DocNodeFolder")
public class DocNodeFolder extends DocNode {

    protected DocNodeFolder() {
    }

    public DocNodeFolder(String name, String creatorCode, DocBook book, DocNodeType type) {
        super(name, creatorCode, book, type);
    }

}
