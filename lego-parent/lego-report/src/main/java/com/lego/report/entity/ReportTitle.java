package com.lego.report.entity;

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

@Setter
@Getter
@Entity
@Table(name = "report_title")
public class ReportTitle extends BaseEntity {

    private String sqlKey;
    private String align;
    private int sn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_id")
    private ReportDefinition definition;

    protected ReportTitle() {
    }

    public ReportTitle(String sqlKey, String name, ReportDefinition definition) {
        super(name);
        this.sqlKey = sqlKey;
        this.definition = definition;
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("姓名", StringUtil.toString(name));
        attributes.put("字段", StringUtil.toString(sqlKey));
        attributes.put("序列", StringUtil.toString(sn));
        attributes.put("对齐方式", StringUtil.toString(align));
        attributes.put("定义", EntityUtil.toString(definition));
    }
}
