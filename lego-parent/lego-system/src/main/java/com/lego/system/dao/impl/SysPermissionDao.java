package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.entity.SysPermission;

import java.util.Arrays;
import java.util.List;

public class SysPermissionDao extends GenericDao<SysPermission> implements ISysPermissionDao {

    @Override
    public List<SysPermission> findAll() {
        QueryHandler<SysPermission> query = createQueryHandler();
        query.order("t.sn");
        return query.findList();
    }

    @Override
    public List<String> findAllCode() {
        QueryHandler<String> query = createQueryHandler("SELECT p.code FROM sys_permission p", String.class);
        query.order("p.sn");
        return query.findSqlList();
    }

    @Override
    public List<SysPermission> findByType(String... types) {
        QueryHandler<SysPermission> query = createQueryHandler();
        if (types != null && types.length > 0) {
            query.condition("t.type.code IN (:types)").param("types", Arrays.asList(types));
        }
        query.order("t.sn");
        return query.findList();
    }

    @Override
    public SysPermission findBy(SysPermission parent, String code) {
        QueryHandler<SysPermission> query = createQueryHandler();
        if (parent == null) {
            query.condition("t.parent IS NULL");
        } else {
            query.condition("t.parent = :parent").param("parent", parent);
        }
        query.condition("t.code = :code").param("code", code);
        return query.findUnique();
    }

    @Override
    public List<SysPermission> findBy(String employeeCode, String... types) {
        QueryHandler<SysPermission> query = createQueryHandler("SELECT p.* FROM sys_permission p");
        query.join("sys_simple_type pt ON pt.id = p.type_id");
        query.join("sys_role_permission rp ON rp.permission_id = p.id");
        query.join("sys_employee_role er ON er.role_id = rp.role_id");
        query.join("sys_employee e ON e.id = er.employee_id");
        query.condition("e.code = :employeeCode").param("employeeCode", employeeCode);
        if (types != null && types.length > 0) {
            query.condition("pt.code IN (:typeCode)").param("typeCode", Arrays.asList(types));
        }
        query.order("p.sn");
        return query.findSqlList();
    }

    @Override
    public List<String> findSortCodeBy(String employeeCode, String... types) {
        QueryHandler<String> query = createQueryHandler("SELECT p.code FROM sys_permission p", String.class);
        query.join("sys_simple_type pt ON pt.id = p.type_id");
        query.join("sys_app_sort aps ON aps.permission_id = p.id");
        query.join("sys_employee e ON e.id = aps.employee_id");
        query.condition("e.code = :employeeCode").param("employeeCode", employeeCode);
        query.condition("pt.code IN (:typeCode)").param("typeCode", Arrays.asList(types));
        query.order("aps.sn");
        return query.findSqlList();
    }

    @Override
    public List<String> findCodeByRole(String roleCode) {
        QueryHandler<String> query = createQueryHandler("SELECT p.code FROM sys_permission p", String.class);
        query.join("sys_role_permission rp ON rp.permission_id = p.id");
        query.join("sys_role r ON r.id = rp.role_id");
        query.condition("r.code = :roleCode").param("roleCode", roleCode);
        query.order("p.sn");
        return query.findSqlList();
    }

    @Override
    public int findMaxSn(String appCode) {
        QueryHandler<Integer> query = createQueryHandler("SELECT max(p.SN) sn FROM sys_permission p", Integer.class);
        query.condition("p.code LIKE :appCode").param("appCode", appCode + "%");
        query.order("p.sn");
        return query.findSqlUnique();
    }

    @Override
    public List<SysPermission> findByParent(SysPermission parent) {
        QueryHandler<SysPermission> query = createQueryHandler();
        query.condition("t.parent = :parent").param("parent", parent);
        return query.findList();
    }

    @Override
    public List<SysPermission> findByRouteType(String typeCode) {
        QueryHandler<SysPermission> query = createQueryHandler();
        if (StringUtil.isNotBlank(typeCode)) {
            query.condition("t.routeType.code = :typeCode").param("typeCode", typeCode);
        }
        query.order("t.sn");
        return query.findList();
    }

}
