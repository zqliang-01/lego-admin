package com.lego.system.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.EntityUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "sys_custom_form")
public class SysCustomForm extends BaseEntity {

	private boolean enable;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private SysPermission permission;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private SysGenTable table;

	protected SysCustomForm() { }

	public SysCustomForm(String name) {
		super(name);
		this.enable = true;
	}

	@Override
	protected void doBuildReadableSnapshot(Map<String, String> attributes) {
		attributes.put("编码", getCode());
		attributes.put("名称", getName());
		attributes.put("关联菜单", EntityUtil.toString(permission));
		attributes.put("数据表", EntityUtil.toString(table));
		attributes.put("状态", enable ? "生效" : "失效");
	}
}
