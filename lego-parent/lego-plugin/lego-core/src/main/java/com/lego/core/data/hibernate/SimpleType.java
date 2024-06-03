package com.lego.core.data.hibernate;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class SimpleType extends BaseEntity {

    private Integer serialNumber;

    protected SimpleType() {
    }

}
