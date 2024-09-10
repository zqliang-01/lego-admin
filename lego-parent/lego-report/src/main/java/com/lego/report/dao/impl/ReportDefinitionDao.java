package com.lego.report.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.util.StringUtil;
import com.lego.report.dao.IReportDefinitionDao;
import com.lego.report.entity.ReportDefinition;

import java.util.List;

public class ReportDefinitionDao extends GenericDao<ReportDefinition> implements IReportDefinitionDao {

    @Override
    public List<ReportDefinition> findBy(String code, String name, String type, Boolean enable) {
        QueryHandler<ReportDefinition> query = createQueryHandler();
        if (StringUtil.isNotBlank(code)) {
            query.condition("t.code = :code").param("code", code);
        }
        if (StringUtil.isNotBlank(name)) {
            query.condition("t.name LIKE :name").param("name", "%" + name + "%");
        }
        if (StringUtil.isNotBlank(type)) {
            query.condition("t.type = :type").param("type", type);
        }
        if (enable != null) {
            query.condition("t.enable = :enable").param("enable", enable);
        }
        query.order("t.sn");
        return query.findList();
    }
}
