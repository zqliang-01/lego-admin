package com.lego.core.data;

import com.lego.core.data.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

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
