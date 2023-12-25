package com.lego.system.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SysPermissionType")
public class SysPermissionType extends SysSimpleType {

	protected SysPermissionType() { }
}
