package com.lego.report.vo;


import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReportDefinitionCreateVO extends VO {

    private static final long serialVersionUID = 1L;

    private String name;
    private String dataSource;
    private int sn;
    private boolean enable;
    private String sqlText;
    private int maxExportSize;

    private List<ReportTitleVO> titles = new ArrayList<>();
    private List<ReportConditionVO> params = new ArrayList<>();
}
