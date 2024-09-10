package com.lego.report.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportDesignSortVO extends VO {

    private List<ReportDesignSortItemVO> leftSort;
    private List<ReportDesignSortItemVO> rightSort;

}
