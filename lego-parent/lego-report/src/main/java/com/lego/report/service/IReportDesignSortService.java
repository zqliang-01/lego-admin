package com.lego.report.service;

import com.lego.report.vo.ReportDesignSortVO;

public interface IReportDesignSortService {

    void changeSort(String loginCode, ReportDesignSortVO vo);
}
