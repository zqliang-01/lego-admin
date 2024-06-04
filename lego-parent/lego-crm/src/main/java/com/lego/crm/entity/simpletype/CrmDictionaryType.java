package com.lego.crm.entity.simpletype;

import com.lego.core.util.StringUtil;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("CrmDictionaryType")
public class CrmDictionaryType extends CrmSimpleType {

    protected CrmDictionaryType() {
    }

    public CrmDictionaryType(String name, int serialNumber) {
        super(name, serialNumber);
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("序号", StringUtil.toString(getSerialNumber()));
    }
}
