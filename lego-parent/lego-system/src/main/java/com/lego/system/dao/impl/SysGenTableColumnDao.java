package com.lego.system.dao.impl;

import java.util.List;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.entity.SysGenTableColumn;

public class SysGenTableColumnDao extends GenericDao<SysGenTableColumn> implements ISysGenTableColumnDao {

	@Override
	public List<SysGenTableColumn> findBy(String tableCode) {
		QueryHandler<SysGenTableColumn> query = createQueryHandler();
		query.condition("t.table.code = :tableCode").param("tableCode", tableCode);
		query.order("t.sn");
		return query.findList();
	}

}
