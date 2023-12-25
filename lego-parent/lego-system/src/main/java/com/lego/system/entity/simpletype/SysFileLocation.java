package com.lego.system.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SysFileLocation")
public class SysFileLocation extends SysSimpleType {

	protected SysFileLocation() { }
}
