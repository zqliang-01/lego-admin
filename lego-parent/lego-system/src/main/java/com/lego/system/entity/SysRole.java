package com.lego.system.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.EntityUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "sys_role")
public class SysRole extends BaseEntity {

    @Setter
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "sys_role_permission", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id") })
    private List<SysPermission> permissions = new ArrayList<SysPermission>();

    protected SysRole() { }

    public SysRole(String code, String name) {
    	super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
    	attributes.put("编码", getCode());
    	attributes.put("名称", getName());
    	attributes.put("权限", EntityUtil.toString(permissions));
    }
}
