package com.lego.crm.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CrmDictionaryType")
public class CrmDictionaryType extends CrmSimpleType {

	protected CrmDictionaryType() { }
}
