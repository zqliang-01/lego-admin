package com.lego.report.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lego.report.dto.ReportDesignOpenInfo;
import com.lego.report.vo.ReportExportVO;
import com.lego.report.vo.ReportOpenDashBoardVO;
import com.lego.report.vo.ReportOpenPageVO;

import java.util.List;

public interface IReportOpenService {

    <M> IPage<M> openPageSql(String code, ReportOpenPageVO vo);

    <M> List<M> openConditionList(String code, String conditionCode);

    <M> ReportDesignOpenInfo<M> openDashBoardSql(ReportOpenDashBoardVO vo);

    void export(ReportExportVO exportVO);
}
