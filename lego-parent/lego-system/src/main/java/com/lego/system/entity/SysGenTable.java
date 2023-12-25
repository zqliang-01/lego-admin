package com.lego.system.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.data.hibernate.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sys_gen_table")
public class SysGenTable extends BaseEntity {

	private String comment;
	private String appCode;
	private String urlName;
	private String className;
	private String fieldName;
	private String permissionCode;
	private String packageName;
	private String path;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
	private SysEmployee creator;

	protected SysGenTable() { }

	public SysGenTable(String code, String name) {
		super(code, name);
	}

	@Override
	protected void doBuildReadableSnapshot(Map<String, String> attributes) {
		attributes.put("表名", this.code);
		attributes.put("功能名称", getName());
		attributes.put("描述", this.comment);
		attributes.put("包名", this.packageName);
		attributes.put("模块名", this.appCode);
		attributes.put("类名", this.className);
		attributes.put("属性名称", this.fieldName);
		attributes.put("资源名称", this.urlName);
		attributes.put("权限编码", this.permissionCode);
		attributes.put("生成路径", this.path);
	}

	public String getQualifiedName() {
		return packageName + ".entity." + className;
	}
}
