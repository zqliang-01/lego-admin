package com.lego.report.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.report.entity.ReportDefinition;
import com.lego.report.entity.ReportTitle;

import java.util.List;

public interface IReportTitleDao extends IGenericDao<ReportTitle> {

    List<ReportTitle> findBy(ReportDefinition definition);
}
