package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysFile;

public interface ISysFileDao extends IGenericDao<SysFile> {

	List<SysFile> findBy(String entityCode, String permissionCode);
}
