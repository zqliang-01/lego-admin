package com.lego.report.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.report.dao.IReportConditionDao;
import com.lego.report.entity.ReportCondition;
import com.lego.report.entity.ReportDefinition;

import java.util.List;

public class ReportConditionDao extends GenericDao<ReportCondition> implements IReportConditionDao {

    @Override
    public List<ReportCondition> findBy(ReportDefinition definition) {
        QueryHandler<ReportCondition> query = createQueryHandler();
        query.condition("t.definition = :definition").param("definition", definition);
        query.order("t.sn");
        return query.findList();
    }

}
