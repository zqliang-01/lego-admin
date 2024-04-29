package com.lego.report.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportTitleVO extends VO {

    private String sqlKey;
    private String name;
    private String align;
    private int sn;
}
