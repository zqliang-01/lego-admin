package com.lego.sharding.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.data.hibernate.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sharding_properties")
public class ShardingProperties extends BaseEntity {

    private boolean enable;
	private String entityCode;
    private String description;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id", referencedColumnName = "id")
	private ShardingConfig config;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
	private ShardingTemplate template;

	protected ShardingProperties() { }

	public ShardingProperties(String code, String name) {
		super(code, name);
		this.enable = true;
	}
}
