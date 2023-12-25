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

@Getter
@Setter
@Entity
@Table(name = "sys_scene")
public class SysScene extends BaseEntity {

	private String data;
    private boolean visible;
    private boolean current;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", referencedColumnName = "id")
    private SysCustomForm form;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private SysEmployee employee;

	protected SysScene() { }

	public SysScene(String name) {
		super(name);
		this.visible = true;
	}

	@Override
	protected void doBuildReadableSnapshot(Map<String, String> attributes) {
		attributes.put("编码", getCode());
		attributes.put("名称", getName());
		attributes.put("是否默认", current ? "是" : "否");
		attributes.put("归属表单", EntityUtil.toString(form));
		attributes.put("归属员工", EntityUtil.toString(employee));
		attributes.put("条件内容", data);
	}
}
