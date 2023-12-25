package com.lego.core.data.hibernate;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BusiEntity extends BaseEntity {

	private Date updateTime;
	private String creatorCode;

	protected BusiEntity() { }

	protected BusiEntity(String name) {
		super(name);
	}

	protected BusiEntity(String code, String name) {
		super(code, name);
	}
}
