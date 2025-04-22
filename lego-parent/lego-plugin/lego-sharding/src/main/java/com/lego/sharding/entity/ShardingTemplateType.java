package com.lego.sharding.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.lego.core.data.hibernate.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sharding_template_type")
public class ShardingTemplateType extends BaseEntity {

    private boolean enable;
    private String description;

	protected ShardingTemplateType() { }
}
