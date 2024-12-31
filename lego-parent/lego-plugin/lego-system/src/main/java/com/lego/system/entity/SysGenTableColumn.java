package com.lego.system.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

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

    @ElementCollection
    @Column(name = "value")
    @MapKeyColumn(name = "name")
    @CollectionTable(name = "sys_gen_table_column_attr", joinColumns = {@JoinColumn(name = "column_id")})
    private Map<String, String> attributes = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private SysGenTable table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relative_table_id", referencedColumnName = "id")
    private SysGenTable relativeTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private SysEmployee creator;

    protected SysGenTableColumn() {
    }

    public SysGenTableColumn(String name) {
        super(name);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("列名", this.getName());
        attributes.put("描述", this.comment);
        attributes.put("数据类型", this.dataType);
        attributes.put("表单字段类型", this.formType);
        attributes.put("JAVA字段名", this.javaField);
        attributes.put("JAVA类型", this.javaFieldType);
        attributes.put("是否必填", required ? "是" : "否");
        attributes.put("是否唯一", uniqueness ? "是" : "否");
        attributes.put("序号", StringUtil.toString(sn));
        attributes.put("关联表", EntityUtil.toString(relativeTable));
        attributes.put("扩展属性", StringUtil.toString(getAttributes()));
    }

    public void removeAttribute() {
        this.attributes = new HashMap<>();
    }
}
