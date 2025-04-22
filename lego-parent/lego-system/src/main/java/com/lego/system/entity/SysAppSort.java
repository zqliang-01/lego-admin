package com.lego.system.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
	protected void doBuildReadableSnapshot(ReadableVO attributes) {
		attributes.put("序号", StringUtil.toString(sn));
		attributes.put("权限", EntityUtil.toString(permission));
		attributes.put("员工", EntityUtil.toString(employee));
	}
}
