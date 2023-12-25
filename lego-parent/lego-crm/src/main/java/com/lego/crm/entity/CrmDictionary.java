package com.lego.crm.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.crm.entity.simpletype.CrmDictionaryType;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter@Entity
@Table(name = "crm_dictionaries")
public class CrmDictionary extends BaseEntity {

    private Integer serialNumber;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
	private CrmDictionaryType type;

	protected CrmDictionary() { }

	public CrmDictionary(String code, String name, CrmDictionaryType type) {
		super(code, name);
		this.type = type;
	}
}
