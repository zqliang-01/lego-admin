package com.lego.system.entity;

import com.lego.core.data.hibernate.entity.TreeEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

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

    @Setter
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<SysDept> children = new HashSet<>();

    protected SysDept() {
    }

    public SysDept(String code, String name) {
        super(code, name);
        this.enable = true;
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", getCode());
        attributes.put("名称", getName());
        attributes.put("上级部门", EntityUtil.toString(getParent()));
        attributes.put("负责人", EntityUtil.toString(leader));
        attributes.put("状态", enable ? "生效" : "失效");
        attributes.put("序号", StringUtil.toString(serialNumber));
    }
}
