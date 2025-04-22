package com.lego.system.entity.simpletype;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SysFileType")
public class SysFileType extends SysSimpleType {

	protected SysFileType() { }
}
