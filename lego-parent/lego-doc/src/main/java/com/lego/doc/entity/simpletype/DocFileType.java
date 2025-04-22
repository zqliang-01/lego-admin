package com.lego.doc.entity.simpletype;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DocFileType")
public class DocFileType extends DocSimpleType {

    protected DocFileType() {
    }
}
