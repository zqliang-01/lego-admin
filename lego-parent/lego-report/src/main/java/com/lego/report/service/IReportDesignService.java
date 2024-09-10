package com.lego.report.service;

import com.lego.report.dto.ReportDesignInfo;
import com.lego.report.vo.ReportDesignCreateVO;
import com.lego.report.vo.ReportDesignModifyVO;
import com.lego.report.vo.ReportDesignSortModifyVO;

import java.util.List;

public interface IReportDesignService {

    List<ReportDesignInfo> findAll();

    List<ReportDesignInfo> findValid(String loginCode);

    ReportDesignInfo findByCode(String code);

    void add(String loginCode, ReportDesignCreateVO vo);

    void update(String loginCode, ReportDesignModifyVO vo);

    void updateSort(String loginCode, ReportDesignSortModifyVO vo);

    void delete(String loginCode, String code);
}
