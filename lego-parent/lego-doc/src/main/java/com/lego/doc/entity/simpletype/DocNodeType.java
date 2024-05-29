package com.lego.doc.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DocNodeType")
public class DocNodeType extends DocSimpleType {

    protected DocNodeType() {
    }
}
