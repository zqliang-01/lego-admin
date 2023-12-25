package com.lego.sharding.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

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
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("状态", enable ? "是" : "否");
        attributes.put("描述", StringUtil.toString(description));
        attributes.put("模板内容", StringUtil.toString(json));
        attributes.put("模板类型", EntityUtil.toString(type));
    }
}
