package com.lego.doc.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DocFileType")
public class DocFileType extends DocSimpleType {

    protected DocFileType() {
    }
}
