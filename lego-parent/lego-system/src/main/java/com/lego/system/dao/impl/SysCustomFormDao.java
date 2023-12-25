package com.lego.system.dao.impl;

import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.data.hibernate.jpa.GenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysGenTable;
import com.lego.system.vo.SysCustomFormSearchVO;

public class SysCustomFormDao extends GenericDao<SysCustomForm> implements ISysCustomFormDao {

	@Override
	public LegoPage<SysCustomForm> findBy(SysCustomFormSearchVO vo) {
		QueryHandler<SysCustomForm> handler = createQueryHandler();
		if (StringUtil.isNotBlank(vo.getCode())) {
			handler.condition("t.code = :code").param("code", vo.getCode());
		}
		if (StringUtil.isNotBlank(vo.getName())) {
			handler.condition("t.name LIKE :name").param("name", "%" + vo.getName() + "%");
		}
		handler.order("t.createTime DESC");
		return handler.findPage(vo);
	}

	@Override
	public SysCustomForm findByPermission(String permissionCode) {
		QueryHandler<SysCustomForm> handler = createQueryHandler();
		handler.condition("t.permission.code = :permissionCode").param("permissionCode", permissionCode);
		return handler.findUnique();
	}

	@Override
	public SysCustomForm findByTable(SysGenTable table) {
		QueryHandler<SysCustomForm> handler = createQueryHandler();
		handler.condition("t.table = :table").param("table", table);
		return handler.findFirst();
	}

	@Override
	public SysCustomForm findByTableCode(String tableCode) {
		QueryHandler<SysCustomForm> handler = createQueryHandler();
		handler.condition("t.table.code = :tableCode").param("tableCode", tableCode);
		return handler.findFirst();
	}

}
