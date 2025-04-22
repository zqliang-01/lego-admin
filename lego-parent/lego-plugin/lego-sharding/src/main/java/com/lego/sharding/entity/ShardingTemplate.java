package com.lego.sharding.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "sharding_template")
public class ShardingTemplate extends BaseEntity {

    private boolean enable;
    private String description;
    private String json;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ShardingTemplateType type;

    protected ShardingTemplate() { }

    public ShardingTemplate(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("状态", enable ? "是" : "否");
        attributes.put("描述", StringUtil.toString(description));
        attributes.put("模板内容", StringUtil.toString(json));
        attributes.put("模板类型", EntityUtil.toString(type));
    }
}
