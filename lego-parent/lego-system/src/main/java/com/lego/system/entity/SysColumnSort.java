package com.lego.system.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", getCode());
        attributes.put("名称", getName());
        attributes.put("表单属性", EntityUtil.toString(field));
        attributes.put("归属员工", EntityUtil.toString(employee));
        attributes.put("宽度", StringUtil.toString(width));
        attributes.put("是否显示", visible ? "是" : "否");
        attributes.put("序号", StringUtil.toString(sn));
    }
}
