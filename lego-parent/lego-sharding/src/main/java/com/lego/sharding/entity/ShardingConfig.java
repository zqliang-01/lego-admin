package com.lego.sharding.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "sharding_config")
public class ShardingConfig extends BaseEntity {

    private boolean enable;
    private String description;

    protected ShardingConfig() { }

    public ShardingConfig(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("生效", enable ? "是" : "否");
        attributes.put("描述", StringUtil.toString(description));
    }
}
