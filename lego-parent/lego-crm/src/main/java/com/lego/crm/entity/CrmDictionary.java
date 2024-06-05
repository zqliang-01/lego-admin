package com.lego.crm.entity;

import com.lego.core.data.hibernate.Dictionary;
import com.lego.crm.entity.simpletype.CrmDictionaryType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "crm_dictionaries")
public class CrmDictionary extends Dictionary<CrmDictionaryType> {

    protected CrmDictionary() {
    }

    public CrmDictionary(String name, CrmDictionaryType type, Integer serialNumber) {
        super(name, type, serialNumber);
    }
}
