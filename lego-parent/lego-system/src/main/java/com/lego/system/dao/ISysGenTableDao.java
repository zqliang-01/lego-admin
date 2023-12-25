package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysGenTable;

public interface ISysGenTableDao extends IGenericDao<SysGenTable> {

	List<SysGenTable> findNotExists();

}
