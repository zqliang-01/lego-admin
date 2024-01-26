package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysPermission;
import com.lego.system.entity.SysRole;
import com.lego.system.vo.SysRoleSearchVO;

import java.util.List;

public interface ISysRoleDao extends IGenericDao<SysRole> {

    List<SysRole> findBy(SysRoleSearchVO vo);

    List<String> findCodesBy(SysPermission permission);
}
