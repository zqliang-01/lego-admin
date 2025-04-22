package com.lego.system.entity.simpletype;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SysFileLocation")
public class SysFileLocation extends SysSimpleType {

	protected SysFileLocation() { }
}
