package com.lego.system.entity.simpletype;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SysPermissionRouteType")
public class SysPermissionRouteType extends SysSimpleType {

    protected SysPermissionRouteType() {
    }
}
