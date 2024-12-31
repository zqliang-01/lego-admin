package com.lego.system.entity.simpletype;

import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SysDictionaryType")
public class SysDictionaryType extends SysSimpleType {

    protected SysDictionaryType() {
    }

    public SysDictionaryType(String name, int serialNumber) {
        super(name, serialNumber);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("序号", StringUtil.toString(getSerialNumber()));
    }
}
