package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysGenTable;

import java.util.List;

public interface ISysGenTableDao extends IGenericDao<SysGenTable> {

    List<SysGenTable> findNotExists();
}
