package com.lego.system.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sys_gen_table_column")
public class SysGenTableColumn extends BaseEntity {

	private String comment;
	private String dataType;
	private String formType;
	private String javaField;
	private String javaFieldType;
	private boolean required;
	private boolean uniqueness;
	private int sn;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", referencedColumnName = "id")
	private SysGenTable table;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relative_table_id", referencedColumnName = "id")
    private SysGenTable relativeTable;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
	private SysEmployee creator;

	protected SysGenTableColumn() { }

	public SysGenTableColumn(String name) {
		super(name);
	}

	@Override
	protected void doBuildReadableSnapshot(Map<String, String> attributes) {
		attributes.put("列名", this.getName());
		attributes.put("描述", this.comment);
		attributes.put("数据类型", this.dataType);
		attributes.put("表单字段类型", this.formType);
		attributes.put("JAVA字段名", this.javaField);
		attributes.put("是否必填", required ? "是" : "否");
		attributes.put("是否唯一", uniqueness ? "是" : "否");
		attributes.put("序号", StringUtil.toString(sn));
	}
}
