package com.lego.report.entity;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "report_design_sort")
public class ReportDesignSort extends BaseEntity {

    private int sn;
    private boolean enable;
    private String creatorCode;
    private String designCode;

    protected ReportDesignSort() {
    }

    public ReportDesignSort(String creatorCode, String designCode) {
        super("");
        this.designCode = designCode;
        this.creatorCode = creatorCode;
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("图表", this.designCode);
        attributes.put("用户", this.creatorCode);
        attributes.put("状态", StringUtil.toString(this.enable));
        attributes.put("序号", StringUtil.toString(this.sn));
    }
}
