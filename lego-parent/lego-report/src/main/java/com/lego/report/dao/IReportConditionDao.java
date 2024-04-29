package com.lego.report.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.report.entity.ReportCondition;
import com.lego.report.entity.ReportDefinition;

import java.util.List;

public interface IReportConditionDao extends IGenericDao<ReportCondition> {

    List<ReportCondition> findBy(ReportDefinition definition);
}
