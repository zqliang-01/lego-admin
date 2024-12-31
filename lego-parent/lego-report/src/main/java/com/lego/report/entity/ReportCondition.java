package com.lego.report.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "report_condition")
public class ReportCondition extends BaseEntity {

    private String sqlKey;
    private String type;
    private boolean enable;
    private boolean required;
    private String defaultValue;
    private String dependentCode;
    private int sn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_id")
    private ReportDefinition definition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_definition_id")
    private ReportDefinition dataDefinition;

    protected ReportCondition() {
    }

    public ReportCondition(String sqlKey, String name, ReportDefinition definition) {
        super(name);
        this.sqlKey = sqlKey;
        this.enable = true;
        this.definition = definition;
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("姓名", StringUtil.toString(name));
        attributes.put("字段", StringUtil.toString(sqlKey));
        attributes.put("类型", StringUtil.toString(type));
        attributes.put("序列", StringUtil.toString(sn));
        attributes.put("是否生效", enable ? "是" : "否");
        attributes.put("是否必须", required ? "是" : "否");
        attributes.put("默认值", StringUtil.toString(defaultValue));
        attributes.put("报表定义", EntityUtil.toString(definition));
        attributes.put("依赖条件", StringUtil.toString(dependentCode));
        attributes.put("数据集定义", EntityUtil.toString(dataDefinition));
    }
}
