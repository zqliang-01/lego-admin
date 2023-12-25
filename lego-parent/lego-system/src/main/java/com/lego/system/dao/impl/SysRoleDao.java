package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysRole;
import com.lego.system.vo.SysRoleSearchVO;

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

}
