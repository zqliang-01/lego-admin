package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysRole;
import com.lego.system.vo.SysRoleSearchVO;

public interface ISysRoleDao extends IGenericDao<SysRole> {

	List<SysRole> findBy(SysRoleSearchVO vo);
}
