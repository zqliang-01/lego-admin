package com.lego.doc.entity.simpletype;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DocFileLocation")
public class DocFileLocation extends DocSimpleType {

    protected DocFileLocation() {
    }
}
