package com.lego.system.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SysFileType")
public class SysFileType extends SysSimpleType {

	protected SysFileType() { }
}
