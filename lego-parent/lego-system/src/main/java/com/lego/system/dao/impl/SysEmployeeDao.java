package com.lego.system.dao.impl;

import com.lego.core.common.Constants;
import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysEmployeeSearchVO;

public class SysEmployeeDao extends GenericDao<SysEmployee> implements ISysEmployeeDao {

    @Override
    public LegoPage<SysEmployee> findBy(SysEmployeeSearchVO vo) {
        QueryHandler<SysEmployee> query = createQueryHandler();
        if (!Constants.ADMIN_EMPLOYEE_CODE.equals(Constants.loginCode.get())) {
            query.condition("t.code <> :adminCode").param("adminCode", Constants.ADMIN_EMPLOYEE_CODE);
        }
        if (StringUtil.isNotBlank(vo.getCode())) {
            query.condition("t.code = :code").param("code", vo.getCode());
        }
        if (StringUtil.isNotBlank(vo.getName())) {
            query.condition("t.name LIKE :name").param("name", "%" + vo.getName() + "%");
        }
        if (StringUtil.isNotBlank(vo.getDeptCode())) {
            query.condition("t.dept.code = :deptCode").param("deptCode", vo.getDeptCode());
        }
        if (StringUtil.isNotBlank(vo.getRoleCode())) {
            query.condition("EXISTS (SELECT 1 FROM t.roles r WHERE r.code = :roleCode)").param("roleCode", vo.getRoleCode());
        }
        if (vo.getEnable() != null) {
            query.condition("t.enable = :enable").param("enable", vo.getEnable());
        }
        return query.findPage(vo);
    }

}
