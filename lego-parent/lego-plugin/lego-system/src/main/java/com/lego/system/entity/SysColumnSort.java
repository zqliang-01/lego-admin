package com.lego.system.entity;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Map;

@Getter
@Entity
@Table(name = "sys_column_sort")
public class SysColumnSort extends BaseEntity {

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_field_id", referencedColumnName = "id")
    private SysCustomField field;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private SysEmployee employee;

    @Setter
    private Integer width;
    @Setter
    private boolean visible;
    @Setter
    private int sn;

    protected SysColumnSort() {
    }

    public SysColumnSort(SysCustomField field, SysEmployee employee) {
        super(field.getName());
        this.field = field;
        this.employee = employee;
        this.visible = true;
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", getCode());
        attributes.put("名称", getName());
        attributes.put("表单属性", EntityUtil.toString(field));
        attributes.put("归属员工", EntityUtil.toString(employee));
        attributes.put("宽度", StringUtil.toString(width));
        attributes.put("是否显示", visible ? "是" : "否");
        attributes.put("序号", StringUtil.toString(sn));
    }
}
