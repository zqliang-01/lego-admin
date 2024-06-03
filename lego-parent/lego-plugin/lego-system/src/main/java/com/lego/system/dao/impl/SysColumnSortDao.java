package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysColumnSortDao;
import com.lego.system.entity.SysColumnSort;

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

}
