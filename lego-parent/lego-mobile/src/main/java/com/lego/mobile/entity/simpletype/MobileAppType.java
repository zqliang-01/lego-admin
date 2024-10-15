package com.lego.mobile.entity.simpletype;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MobileAppType")
public class MobileAppType extends MobileSimpleType {

    protected MobileAppType() {
    }
}
