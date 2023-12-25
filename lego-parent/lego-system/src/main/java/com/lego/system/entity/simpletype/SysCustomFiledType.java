package com.lego.system.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SysCustomFiledType")
public class SysCustomFiledType extends SysSimpleType {

	protected SysCustomFiledType() { }
}
