package com.lego.report.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("图表", this.designCode);
        attributes.put("用户", this.creatorCode);
        attributes.put("状态", StringUtil.toString(this.enable));
        attributes.put("序号", StringUtil.toString(this.sn));
    }
}
