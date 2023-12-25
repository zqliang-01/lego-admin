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
import com.lego.system.entity.simpletype.SysFileLocation;
import com.lego.system.entity.simpletype.SysFileType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sys_file")
public class SysFile extends BaseEntity {

	private long size;
	private String path;
	private String entityCode;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
	private SysFileType type;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
	private SysFileLocation location;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
	private SysEmployee creator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_id", referencedColumnName = "id")
	private SysPermission permission;

	protected SysFile() { }

	public SysFile(String name, String entityCode) {
		super(name);
		this.entityCode = entityCode;
	}

	@Override
	protected void doBuildReadableSnapshot(Map<String, String> attributes) {
		attributes.put("名称", getName());
		attributes.put("大小", StringUtil.toString(size));
		attributes.put("路径", path);
		attributes.put("领域对象", entityCode);
		attributes.put("类型", EntityUtil.toString(type));
		attributes.put("位置", EntityUtil.toString(location));
		attributes.put("创建人", EntityUtil.toString(creator));
		attributes.put("菜单", EntityUtil.toString(permission));
	}
}
