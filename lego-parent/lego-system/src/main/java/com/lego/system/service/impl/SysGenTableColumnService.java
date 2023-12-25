package com.lego.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BusiService;
import com.lego.system.action.ModifySysGenTableColumnAction;
import com.lego.system.assembler.SysGenTableColumnAssembler;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.dto.SysGenTableColumnInfo;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.service.ISysGenTableColumnService;
import com.lego.system.vo.SysGenTableColumnModifyVO;

@Service
public class SysGenTableColumnService extends BusiService<ISysGenTableColumnDao, SysGenTableColumnAssembler> implements ISysGenTableColumnService {

	@Override
	public List<SysGenTableColumnInfo> findByTable(String tableCode) {
		List<SysGenTableColumn> columns = dao.findBy(tableCode);
		return assembler.create(columns);
	}

	@Override
	public void modify(String operatorCode, List<SysGenTableColumnModifyVO> vos) {
		for (SysGenTableColumnModifyVO vo : vos) {
			new ModifySysGenTableColumnAction(operatorCode, vo).run();
		}
	}

}
