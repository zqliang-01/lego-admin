package com.lego.report.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
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
import javax.persistence.Table;
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
