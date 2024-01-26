package com.lego.system.service;

import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysGenTableColumnInfo;
import com.lego.system.vo.SysGenTableColumnModifyVO;

import java.util.List;

public interface ISysGenTableColumnService {

    List<SysGenTableColumnInfo> findByTable(String tableCode);

    List<TypeInfo> findSimpleTypeBy(String tableCode);

    void modify(String operatorCode, List<SysGenTableColumnModifyVO> vo);
}
