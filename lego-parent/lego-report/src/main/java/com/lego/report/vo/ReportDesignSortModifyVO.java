package com.lego.report.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReportDesignSortModifyVO extends VO {

    private List<String> leftSort = new ArrayList<>();
    private List<String> rightSort = new ArrayList<>();
}
