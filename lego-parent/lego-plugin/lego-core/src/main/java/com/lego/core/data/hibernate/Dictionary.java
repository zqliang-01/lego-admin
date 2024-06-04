package com.lego.core.data.hibernate;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class Dictionary extends BaseEntity {

    private Integer serialNumber;

    protected Dictionary() {
    }

    protected Dictionary(String name, Integer serialNumber) {
        super(name);
        this.serialNumber = serialNumber;
    }
}
