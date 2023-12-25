package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysGenTableDao;
import com.lego.system.entity.SysGenTable;

public class SysGenTableDao extends GenericDao<SysGenTable> implements ISysGenTableDao {

	@Override
	public List<SysGenTable> findNotExists() {
		QueryHandler<SysGenTable> query = createQueryHandler("SELECT * FROM sys_gen_table t");
		query.condition("NOT EXISTS (SELECT 1 FROM sys_custom_form c WHERE c.table_id = t.id)");
		return query.findSqlList();
	}

}
