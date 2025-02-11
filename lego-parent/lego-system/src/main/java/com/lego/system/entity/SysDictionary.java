package com.lego.system.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import com.lego.system.entity.simpletype.SysDictionaryType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "sys_dictionaries")
public class SysDictionary extends BaseEntity {

    private boolean enable;
    private Integer serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private SysDictionaryType type;

    protected SysDictionary() {
    }

    public SysDictionary(String name, SysDictionaryType type, Integer serialNumber) {
        super(name);
        this.type = type;
        this.enable = true;
        this.serialNumber = serialNumber;
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("序号", StringUtil.toString(getSerialNumber()));
        attributes.put("状态", enable ? "生效" : "失效");
        attributes.put("类型", EntityUtil.toString(type));
    }
}
