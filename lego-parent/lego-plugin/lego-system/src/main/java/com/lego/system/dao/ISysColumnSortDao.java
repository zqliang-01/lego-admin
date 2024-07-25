package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysColumnSort;

import java.util.List;

public interface ISysColumnSortDao extends IGenericDao<SysColumnSort> {

    List<SysColumnSort> findByForm(String formCode, String employeeCode);

    SysColumnSort findByField(String fieldCode, String employeeCode);
}
