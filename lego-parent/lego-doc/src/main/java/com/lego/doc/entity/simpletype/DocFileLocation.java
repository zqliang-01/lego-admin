package com.lego.doc.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DocFileLocation")
public class DocFileLocation extends DocSimpleType {

    protected DocFileLocation() {
    }
}
