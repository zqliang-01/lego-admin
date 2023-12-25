package com.lego.system.service;

import java.util.List;

import com.lego.system.dto.SysGenTableColumnInfo;
import com.lego.system.vo.SysGenTableColumnModifyVO;

public interface ISysGenTableColumnService {

	List<SysGenTableColumnInfo> findByTable(String tableCode);

	void modify(String operatorCode, List<SysGenTableColumnModifyVO> vo);
}
