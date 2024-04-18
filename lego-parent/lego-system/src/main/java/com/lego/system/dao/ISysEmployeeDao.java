package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.system.entity.SysDept;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysEmployeeSearchVO;

import java.util.List;

public interface ISysEmployeeDao extends IGenericDao<SysEmployee> {

    LegoPage<SysEmployee> findBy(SysEmployeeSearchVO vo);

    List<SysEmployee> findBy(List<SysDept> depts);

}
