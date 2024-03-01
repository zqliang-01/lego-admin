package com.lego.system.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lego.core.common.Constants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.lego.core.data.IdGenerator;
import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "sys_employee")
public class SysEmployee extends BaseEntity {

    @Setter
    private boolean enable;
    private String password;

    @Setter
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", referencedColumnName = "id")
    private SysDept dept;

    @Setter
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private SysFile image;

    @Setter
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "sys_employee_role", joinColumns = { @JoinColumn(name = "employee_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private List<SysRole> roles = new ArrayList<SysRole>();

    protected SysEmployee() { }

    public SysEmployee(String name) {
    	super(name);
    	this.enable = true;
    	long code = IdGenerator.getCurrent().nextId("employee");
    	this.code = StringUtil.toNumberString("S", code, 5);
    }

    public boolean checkPassword(String plainPassword) {
        String encoded = StringUtil.getMD5(getCode() + plainPassword);
        return password.equals(encoded);
    }

    public void resetPassword(String password) {
    	this.password = StringUtil.getMD5(getCode() + password);
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
    	attributes.put("工号", getCode());
    	attributes.put("姓名", getName());
    	attributes.put("密码", getPassword());
    	attributes.put("部门", EntityUtil.toString(dept));
    	attributes.put("角色", EntityUtil.toString(roles));
    	attributes.put("头像", EntityUtil.toString(image));
    	attributes.put("状态", enable ? "生效" : "失效");
    }

    public boolean isAdmin() {
        return containRole(Constants.ADMIN_ROLE);
    }

	public boolean containRole(String roleCode) {
		for (SysRole role : roles) {
			if (role.getCode().equals(roleCode)) {
				return true;
			}
		}
		return false;
	}
}
