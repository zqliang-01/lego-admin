package com.lego.system.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SysDictionaryType")
public class SysDictionaryType extends SysSimpleType {

	protected SysDictionaryType() { }
}
