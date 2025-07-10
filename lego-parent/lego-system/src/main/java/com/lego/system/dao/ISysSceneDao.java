package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysScene;

import java.util.List;

public interface ISysSceneDao extends IGenericDao<SysScene> {

    List<SysScene> findByForm(String formCode);

    List<SysScene> findBy(String operatorCode, String formCode, boolean visible);
}
