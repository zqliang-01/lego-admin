package com.lego.system.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.data.TreeEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "sys_dept")
public class SysDept extends TreeEntity<SysDept> {

	private boolean enable;
	private int serialNumber;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
	private SysEmployee leader;

	protected SysDept() { }

	public SysDept(String code, String name) {
		super(code, name);
		this.enable = true;
	}

	@Override
	protected void doBuildReadableSnapshot(Map<String, String> attributes) {
		attributes.put("编码", getCode());
		attributes.put("名称", getName());
		attributes.put("上级部门", EntityUtil.toString(getParent()));
		attributes.put("负责人", EntityUtil.toString(leader));
		attributes.put("状态", enable ? "生效" : "失效");
		attributes.put("序号", StringUtil.toString(serialNumber));
	}
}
