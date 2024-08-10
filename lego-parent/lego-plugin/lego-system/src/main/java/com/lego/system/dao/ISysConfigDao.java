package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysConfig;

public interface ISysConfigDao extends IGenericDao<SysConfig> {

    String findValueBy(String code);
}
