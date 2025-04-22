package com.lego.system.entity.simpletype;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SysCustomFiledType")
public class SysCustomFiledType extends SysSimpleType {

	protected SysCustomFiledType() { }
}
