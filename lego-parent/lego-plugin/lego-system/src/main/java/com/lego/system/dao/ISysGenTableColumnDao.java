package com.lego.system.dao;

import java.util.List;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.system.entity.SysGenTableColumn;

public interface ISysGenTableColumnDao extends IGenericDao<SysGenTableColumn> {

	List<SysGenTableColumn> findBy(String tableCode);
}
