package com.lego.core.data;

import javax.persistence.MappedSuperclass;

import com.lego.core.data.hibernate.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class SimpleType extends BaseEntity {

    private Integer serialNumber;

    protected SimpleType() { }

}
