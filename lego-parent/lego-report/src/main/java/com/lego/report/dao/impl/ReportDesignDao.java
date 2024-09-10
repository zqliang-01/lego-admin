package com.lego.report.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.report.dao.IReportDesignDao;
import com.lego.report.entity.ReportDesign;

import java.util.List;

public class ReportDesignDao extends GenericDao<ReportDesign> implements IReportDesignDao {

    @Override
    public List<ReportDesign> findValid(String loginCode) {
        String sql = "SELECT t.id, t.code, t.name, t.version, t.create_time, t.sn, IFNULL(ds.enable, 1) enable, " +
            "t.creator_code, t.definition_id, t.position, t.chart_type, t.data_dimension " +
            "FROM report_design t";
        QueryHandler<ReportDesign> query = createQueryHandler(sql);
        query.leftJoin("report_design_sort ds ON ds.design_code = t.code AND ds.creator_code = :creatorCode");
        query.param("creatorCode", loginCode);
        query.condition("t.enable = :enable").param("enable", true);
        query.order("ds.sn, t.sn");
        return query.findSqlList();
    }

    @Override
    public List<ReportDesign> findAll() {
        QueryHandler<ReportDesign> query = createQueryHandler();
        query.order("t.sn");
        return query.findList();
    }
}
