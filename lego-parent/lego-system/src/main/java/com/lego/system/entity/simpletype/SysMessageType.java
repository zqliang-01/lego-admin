package com.lego.system.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SysMessageType")
public class SysMessageType extends SysSimpleType {

    protected SysMessageType() {
    }
}
