package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysPermission;

import java.util.List;

public interface ISysPermissionDao extends IGenericDao<SysPermission> {

    List<String> findAllCode();

    List<SysPermission> findByEmployee(String employeeCode);

    List<SysPermission> findByType(String... types);

    List<SysPermission> findBy(String employeeCode, String... types);

    SysPermission findBy(SysPermission parent, String code);

    List<String> findSortCodeBy(String employeeCode, String... types);

    List<String> findCodeByRole(String roleCode);

    int findMaxSn(String appCode);

    List<SysPermission> findByParent(SysPermission parent);
}
