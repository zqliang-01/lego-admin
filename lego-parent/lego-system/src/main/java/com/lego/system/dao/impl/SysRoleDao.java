package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysPermission;
import com.lego.system.entity.SysRole;
import com.lego.system.vo.SysRoleSearchVO;

import java.util.List;

public class SysRoleDao extends GenericDao<SysRole> implements ISysRoleDao {

    @Override
    public List<SysRole> findBy(SysRoleSearchVO vo) {
        QueryHandler<SysRole> query = createQueryHandler();
        if (StringUtil.isNotBlank(vo.getCode())) {
            query.condition("t.code = :code").param("code", vo.getCode());
        }
        if (StringUtil.isNotBlank(vo.getName())) {
            query.condition("t.name LIKE :name").param("name", "%" + vo.getName() + "%");
        }
        return query.findList();
    }

    @Override
    public List<String> findCodesBy(SysPermission permission) {
        QueryHandler<String> query = createQueryHandler("SELECT t.code FROM sys_role t", String.class);
        query.join("sys_role_permission rp ON rp.role_id = t.id");
        query.condition("rp.permission_id = :permissionId").param("permissionId", permission.getId());
        return query.findSqlList();
    }
}
