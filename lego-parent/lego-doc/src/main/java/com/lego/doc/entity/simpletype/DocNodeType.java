package com.lego.doc.entity.simpletype;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DocNodeType")
public class DocNodeType extends DocSimpleType {

    protected DocNodeType() {
    }
}
