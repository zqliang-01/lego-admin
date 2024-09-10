package com.lego.report.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.report.entity.ReportDesign;

import java.util.List;

public interface IReportDesignDao extends IGenericDao<ReportDesign> {

    List<ReportDesign> findValid(String loginCode);
}
