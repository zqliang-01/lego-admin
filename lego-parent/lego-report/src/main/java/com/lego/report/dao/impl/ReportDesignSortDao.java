package com.lego.report.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.report.dao.IReportDesignSortDao;
import com.lego.report.entity.ReportDesignSort;

import java.util.List;

public class ReportDesignSortDao extends GenericDao<ReportDesignSort> implements IReportDesignSortDao {

    @Override
    public List<ReportDesignSort> findBy(String creatorCode) {
        QueryHandler<ReportDesignSort> query = createQueryHandler();
        query.condition("t.creatorCode = :creatorCode").param("creatorCode", creatorCode);
        return query.findList();
    }
}
