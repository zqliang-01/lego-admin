package com.lego.system.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "sys_code_generator")
public class SysCodeGenerator extends BaseEntity {

	private String prefix;
	private int serialLength;
	private String datePattern;

	protected SysCodeGenerator() { }

	public SysCodeGenerator(String name) {
		super(name);
	}

	@Override
	protected void doBuildReadableSnapshot(ReadableVO attributes) {
		attributes.put("名称", StringUtil.toString(name));
		attributes.put("前缀", StringUtil.toString(prefix));
		attributes.put("序列长度", StringUtil.toString(serialLength));
		attributes.put("时间格式", StringUtil.toString(datePattern));
	}
}
