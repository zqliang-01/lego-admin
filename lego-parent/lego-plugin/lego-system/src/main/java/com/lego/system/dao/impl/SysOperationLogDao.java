package com.lego.system.dao.impl;

import com.lego.core.common.Constants;
import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysOperationLogDao;
import com.lego.system.entity.SysOperationLog;
import com.lego.system.vo.SysOperationLogSearchVO;

import java.util.List;

public class SysOperationLogDao extends GenericDao<SysOperationLog> implements ISysOperationLogDao {

    @Override
    public List<SysOperationLog> findBy(String loginCode, String entityCode, String permissionCode) {
        QueryHandler<SysOperationLog> query = createQueryHandler();
        query.condition("t.entityCode = :entityCode").param("entityCode", entityCode);
//		query.condition("t.operator.code = :operatorCode").param("operatorCode", loginCode);
        query.condition("t.permission.code = :permissionCode").param("permissionCode", permissionCode);
        query.order("t.createTime DESC");
        return query.findList();
    }

    @Override
    public LegoPage<SysOperationLog> findPageBy(String loginCode, SysOperationLogSearchVO vo) {
        QueryHandler<SysOperationLog> query = createQueryHandler();
        if (StringUtil.isNotBlank(vo.getDescription())) {
            query.condition("t.description LIKE :description").param("description", "%" + vo.getDescription() + "%");
        }
        if (!Constants.ADMIN_EMPLOYEE_CODE.equals(loginCode)) {
            query.condition("t.operator.code <> :admin").param("admin", Constants.ADMIN_EMPLOYEE_CODE);
        }
        if (StringUtil.isNotBlank(vo.getOperatorCode())) {
            query.condition("t.operator.code = :operator").param("operator", vo.getOperatorCode());
        }
        if (vo.getStartTime() != null) {
            query.condition("t.createTime >= :createTime").param("createTime", vo.getStartTime());
        }
        if (vo.getEndTime() != null) {
            query.condition("t.createTime <= :createTime1").param("createTime1", vo.getEndTime());
        }
        query.order("t.createTime DESC");
        return query.findPage(vo);
    }

}
