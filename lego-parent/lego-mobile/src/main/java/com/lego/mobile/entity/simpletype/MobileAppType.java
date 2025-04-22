package com.lego.mobile.entity.simpletype;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MobileAppType")
public class MobileAppType extends MobileSimpleType {

    protected MobileAppType() {
    }
}
