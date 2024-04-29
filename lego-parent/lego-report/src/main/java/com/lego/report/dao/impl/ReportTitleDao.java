package com.lego.report.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.report.dao.IReportTitleDao;
import com.lego.report.entity.ReportDefinition;
import com.lego.report.entity.ReportTitle;

import java.util.List;

public class ReportTitleDao extends GenericDao<ReportTitle> implements IReportTitleDao {

    @Override
    public List<ReportTitle> findBy(ReportDefinition definition) {
        QueryHandler<ReportTitle> query = createQueryHandler();
        query.condition("t.definition = :definition").param("definition", definition);
        query.order("t.sn");
        return query.findList();
    }
}
