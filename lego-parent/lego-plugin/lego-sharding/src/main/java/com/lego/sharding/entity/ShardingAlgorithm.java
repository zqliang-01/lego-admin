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
@Table(name = "sharding_algorithms")
public class ShardingAlgorithm extends BaseEntity {

    private boolean enable;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private ShardingTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id")
    private ShardingConfig config;

    protected ShardingAlgorithm() { }

    public ShardingAlgorithm(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("生效", enable ? "是" : "否");
        attributes.put("描述", StringUtil.toString(description));
        attributes.put("模板", EntityUtil.toString(template));
        attributes.put("规则", EntityUtil.toString(config));
    }
}
