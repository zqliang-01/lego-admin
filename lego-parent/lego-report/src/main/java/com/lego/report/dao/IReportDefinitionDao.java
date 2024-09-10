package com.lego.report.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.report.entity.ReportDefinition;

import java.util.List;

public interface IReportDefinitionDao extends IGenericDao<ReportDefinition> {

    List<ReportDefinition> findBy(String code, String name, String type, Boolean enable);
}
