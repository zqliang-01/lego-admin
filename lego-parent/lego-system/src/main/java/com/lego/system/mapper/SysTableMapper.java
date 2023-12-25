package com.lego.system.mapper;

import java.util.List;

import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysDBTableColumnInfo;

public interface SysTableMapper {

	List<String> selectDBName();

	List<TypeInfo> selectByDBName(String dbName);

	List<TypeInfo> selectColumnsName(String tableName);

	List<SysDBTableColumnInfo> selectColumns(String tableName);

	String selectCommentByName(String tableName);
}
