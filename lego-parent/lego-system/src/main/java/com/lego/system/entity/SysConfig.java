package com.lego.system.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.lego.core.data.hibernate.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "sys_config")
public class SysConfig extends BaseEntity {

	private String value;
	private boolean enable;

	protected SysConfig() { }
}
