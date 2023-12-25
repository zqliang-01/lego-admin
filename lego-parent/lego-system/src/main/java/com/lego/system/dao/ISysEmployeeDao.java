package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysEmployeeSearchVO;

public interface ISysEmployeeDao extends IGenericDao<SysEmployee> {

	LegoPage<SysEmployee> findBy(SysEmployeeSearchVO vo);

}
