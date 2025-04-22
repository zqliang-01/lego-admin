package com.lego.system.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
	protected void doBuildReadableSnapshot(ReadableVO attributes) {
		attributes.put("编码", getCode());
		attributes.put("名称", getName());
		attributes.put("是否默认", current ? "是" : "否");
		attributes.put("归属表单", EntityUtil.toString(form));
		attributes.put("归属员工", EntityUtil.toString(employee));
		attributes.put("条件内容", data);
	}
}
