package com.lego.system.service;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.MetaTableColumnInfo;
import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysGenTableInfo;
import com.lego.system.vo.SysGenTableCreateVO;
import com.lego.system.vo.SysGenTableModifyVO;
import com.lego.system.vo.SysGenTableSearchVO;

import java.util.List;

public interface ISysGenTableService {

    LegoPage<SysGenTableInfo> findPageBy(SysGenTableSearchVO vo);

    List<TypeInfo> findNotExists();

    List<TypeInfo> findAll();

    List<TypeInfo> findSimpleType();

    SysGenTableInfo findByCode(String code);

    List<TypeInfo> findTableName(String dataSource);

    SysGenTableInfo findInitBy(String code, String dataSource);

    void add(String operatorCode, SysGenTableCreateVO vo, List<MetaTableColumnInfo> tableColumns);

    void modify(String operatorCode, SysGenTableModifyVO vo);

    void sync(String operatorCode, String code, List<MetaTableColumnInfo> tableColumns);

    List<TypeInfo> findDataSource();
}
