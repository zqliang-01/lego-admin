package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysDeptDao;
import com.lego.system.entity.SysDept;
import com.lego.system.vo.SysDeptSearchVO;

public class SysDeptDao extends GenericDao<SysDept> implements ISysDeptDao {

	@Override
	public List<SysDept> findBy(SysDeptSearchVO vo) {
		QueryHandler<SysDept> query = createQueryHandler();
		if (StringUtil.isNotBlank(vo.getCode())) {
			query.condition("t.code = :code").param("code", vo.getCode());
		}
		if (StringUtil.isNotBlank(vo.getName())) {
			query.condition("t.name LIKE :name").param("name", "%" + vo.getName() + "%");
		}
		return query.findList();
	}

}
