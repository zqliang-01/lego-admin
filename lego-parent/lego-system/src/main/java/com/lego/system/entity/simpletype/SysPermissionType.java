package com.lego.system.entity.simpletype;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SysPermissionType")
public class SysPermissionType extends SysSimpleType {

	protected SysPermissionType() { }
}
