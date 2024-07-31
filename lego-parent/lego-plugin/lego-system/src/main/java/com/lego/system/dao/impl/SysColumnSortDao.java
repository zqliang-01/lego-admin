package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.entity.SysColumnSort;
import com.lego.system.entity.SysCustomField;

import java.util.List;

public class SysColumnSortDao extends GenericDao<SysColumnSort> implements ISysColumnSortDao {

    @Override
    public List<SysColumnSort> findByForm(String formCode, String employeeCode) {
        QueryHandler<SysColumnSort> query = createQueryHandler();
        query.condition("t.field.form.code = :formCode").param("formCode", formCode);
        query.condition("t.employee.code = :employeeCode").param("employeeCode", employeeCode);
        query.condition("t.field.hidden = :hidden").param("hidden", false);
        query.order("t.sn");
        return query.findList();
    }

    @Override
    public SysColumnSort findByField(String fieldCode, String employeeCode) {
        QueryHandler<SysColumnSort> query = createQueryHandler();
        query.condition("t.field.code = :fieldCode").param("fieldCode", fieldCode);
        query.condition("t.employee.code = :employeeCode").param("employeeCode", employeeCode);
        query.order("t.sn");
        return query.findUnique();
    }

    @Override
    public List<SysColumnSort> findByField(SysCustomField field) {
        QueryHandler<SysColumnSort> query = createQueryHandler();
        query.condition("t.field = :field").param("field", field);
        return query.findList();
    }

}
