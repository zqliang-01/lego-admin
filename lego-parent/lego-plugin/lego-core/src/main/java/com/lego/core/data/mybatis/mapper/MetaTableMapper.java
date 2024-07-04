package com.lego.core.data.mybatis.mapper;

import com.lego.core.dto.MetaTableColumnInfo;
import com.lego.core.dto.TypeInfo;

import java.util.List;

public interface MetaTableMapper {

    List<String> selectDBName();

    String selectCurrentDBName();

    List<TypeInfo> selectByDBName(String dbName);

    List<TypeInfo> selectColumnsName(String tableName);

    List<MetaTableColumnInfo> selectColumns(String tableName);

    String selectCommentByName(String tableName);
}
