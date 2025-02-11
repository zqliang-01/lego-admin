package com.lego.system.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SysPermissionRouteType")
public class SysPermissionRouteType extends SysSimpleType {

    protected SysPermissionRouteType() {
    }
}
