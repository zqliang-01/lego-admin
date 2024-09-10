package com.lego.report.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReportDesignModifyVO extends VO {

    private int sn;
    private String code;
    private String name;
    private boolean enable;
    private String position;
    private String chartType;
    private String dataDimension;
    private String definition;
    private List<String> dataCategories = new ArrayList<>();
}
