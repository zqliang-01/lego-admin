package com.lego.core.data.hibernate;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BusEntity extends BaseEntity {

    private Date updateTime;
    private String creatorCode;

    protected BusEntity() {
    }

    protected BusEntity(String name) {
        super(name);
    }

    protected BusEntity(String code, String name) {
        super(code, name);
    }
}
