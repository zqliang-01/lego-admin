package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysPrintTemplateDao;
import com.lego.system.entity.SysPrintTemplate;

import java.util.List;

public class SysPrintTemplateDao extends GenericDao<SysPrintTemplate> implements ISysPrintTemplateDao {

    @Override
    public List<SysPrintTemplate> findBy(String formCode) {
        QueryHandler<SysPrintTemplate> query = createQueryHandler();
        query.condition("t.form.code = :formCode").param("formCode", formCode);
        return query.findList();
    }
}
