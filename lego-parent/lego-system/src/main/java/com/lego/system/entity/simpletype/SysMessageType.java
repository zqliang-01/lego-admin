package com.lego.system.entity.simpletype;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SysMessageType")
public class SysMessageType extends SysSimpleType {

    protected SysMessageType() {
    }
}
