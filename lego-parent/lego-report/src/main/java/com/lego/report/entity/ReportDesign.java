package com.lego.report.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "report_design")
public class ReportDesign extends BaseEntity {

    private int sn;
    private boolean enable;
    private String position;
    private String chartType;
    private String dataDimension;
    private String creatorCode;

    @ElementCollection
    @Column(name = "data_category")
    @CollectionTable(name = "report_design_category", joinColumns = @JoinColumn(name = "design_id"))
    private List<String> dataCategories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_id")
    private ReportDefinition definition;

    protected ReportDesign() {
    }

    public ReportDesign(String name) {
        super(name);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("姓名", StringUtil.toString(name));
        attributes.put("序列", StringUtil.toString(sn));
        attributes.put("是否生效", enable ? "是" : "否");
        attributes.put("报表定义", StringUtil.toString(definition));
        attributes.put("图形位置", StringUtil.toString(position));
        attributes.put("图形类型", StringUtil.toString(chartType));
        attributes.put("数据维度", StringUtil.toString(dataDimension));
        attributes.put("创建人", StringUtil.toString(creatorCode));
        attributes.put("数据分类", StringUtil.toString(dataCategories));
    }
}
