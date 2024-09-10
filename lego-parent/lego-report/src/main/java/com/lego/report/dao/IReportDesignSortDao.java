package com.lego.report.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.report.entity.ReportDesignSort;

import java.util.List;

public interface IReportDesignSortDao extends IGenericDao<ReportDesignSort> {

    List<ReportDesignSort> findBy(String creatorCode);
}
