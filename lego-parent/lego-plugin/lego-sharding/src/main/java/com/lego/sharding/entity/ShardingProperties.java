package com.lego.sharding.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "sharding_properties")
public class ShardingProperties extends BaseEntity {

    private boolean enable;
    private Long entityId;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id", referencedColumnName = "id")
    private ShardingConfig config;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    private ShardingTemplate template;

    protected ShardingProperties() {
    }

    public ShardingProperties(String code, String name) {
        super(code, name);
        this.enable = true;
    }
}
