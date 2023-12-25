package com.lego.system.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "sys_app_sort")
public class SysAppSort extends BaseEntity {

	private int sn;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private SysPermission permission;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private SysEmployee employee;

	protected SysAppSort() { }

	public SysAppSort(int sn, SysPermission permission, SysEmployee employee) {
		super(permission.getName());
		this.sn = sn;
		this.permission = permission;
		this.employee = employee;
	}

	@Override
	protected void doBuildReadableSnapshot(Map<String, String> attributes) {
		attributes.put("序号", StringUtil.toString(sn));
		attributes.put("权限", EntityUtil.toString(permission));
		attributes.put("员工", EntityUtil.toString(employee));
	}
}
