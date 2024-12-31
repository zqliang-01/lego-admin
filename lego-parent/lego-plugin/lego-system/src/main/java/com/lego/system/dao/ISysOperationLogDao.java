package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.system.entity.SysOperationLog;
import com.lego.system.vo.SysOperationLogSearchVO;

import java.util.List;

public interface ISysOperationLogDao extends IGenericDao<SysOperationLog> {

	List<SysOperationLog> findBy(String loginCode, String entityCode, String permissionCode);

	LegoPage<SysOperationLog> findPageBy(String loginCode, SysOperationLogSearchVO vo);
}
