package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysDept;
import com.lego.system.vo.SysDeptSearchVO;

import java.util.List;

public interface ISysDeptDao extends IGenericDao<SysDept> {

    List<SysDept> findBy(SysDeptSearchVO vo);

    List<SysDept> findBy(String parentCode);

    List<String> findAllChildrenCode(SysDept dept);
}
