package com.lego.crm.entity;

import com.lego.core.data.hibernate.Dictionary;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.crm.entity.simpletype.CrmDictionaryType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "crm_dictionaries")
public class CrmDictionary extends Dictionary {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private CrmDictionaryType type;

    protected CrmDictionary() {
    }

    public CrmDictionary(String name, CrmDictionaryType type, Integer serialNumber) {
        super(name, serialNumber);
        this.type = type;
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("类型", EntityUtil.toString(type));
        attributes.put("序号", StringUtil.toString(getSerialNumber()));
    }
}
