package com.lego.core.data.hibernate.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class TreeEntity<T extends BaseEntity> extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private T parent;

    protected TreeEntity() {
    }

    protected TreeEntity(String name) {
        super(name);
    }

    protected TreeEntity(String code, String name) {
        super(code, name);
    }

    public Long getParentId() {
        if (parent != null) {
            return parent.getId();
        }
        return null;
    }
}