package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysScene;

public interface ISysSceneDao extends IGenericDao<SysScene> {

	List<SysScene> findByForm(String formCode);

	List<SysScene> findBy(String formCode, boolean visible);
}
