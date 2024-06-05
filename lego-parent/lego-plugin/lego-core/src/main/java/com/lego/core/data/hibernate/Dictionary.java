package com.lego.core.data.hibernate;

import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Map;

@Getter
@Setter
@MappedSuperclass
public abstract class Dictionary<T extends SimpleType> extends BaseEntity {

    private boolean enable;
    private Integer serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private T type;

    protected Dictionary() {
    }

    protected Dictionary(String name, T type, Integer serialNumber) {
        super(name);
        this.type = type;
        this.enable = true;
        this.serialNumber = serialNumber;
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("序号", StringUtil.toString(getSerialNumber()));
        attributes.put("状态", enable ? "生效" : "失效");
        attributes.put("类型", EntityUtil.toString(type));
    }
}
