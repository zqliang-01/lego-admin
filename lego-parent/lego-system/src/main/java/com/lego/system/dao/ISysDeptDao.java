package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysDept;
import com.lego.system.vo.SysDeptSearchVO;

public interface ISysDeptDao extends IGenericDao<SysDept> {

	List<SysDept> findBy(SysDeptSearchVO vo);
}
