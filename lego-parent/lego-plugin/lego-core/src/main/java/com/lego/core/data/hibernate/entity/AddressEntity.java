package com.lego.core.data.hibernate.entity;

import com.lego.core.dto.AddressData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressEntity {

    private String province;
    private String city;
    private String area;
    private String detail;

    @Override
    public String toString() {
        return AddressData.getName(province)
            + AddressData.getName(city)
            + AddressData.getName(area)
            + detail;
    }
}
