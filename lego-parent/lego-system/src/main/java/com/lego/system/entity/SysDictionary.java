package com.lego.system.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.system.entity.simpletype.SysDictionaryType;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter@Entity
@Table(name = "sys_dictionaries")
public class SysDictionary extends BaseEntity {

    private Integer serialNumber;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
	private SysDictionaryType type;

	protected SysDictionary() { }

	public SysDictionary(String code, String name, SysDictionaryType type) {
		super(code, name);
		this.type = type;
	}
}
