package com.lego.core.data.mybatis.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.lego.core.util.StringUtil;

public class SelectAll extends AbstractMethod {

	private static final long serialVersionUID = 1L;

	protected SelectAll() {
		super("selectAll");
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		String sql = "SELECT %s FROM %s ";
		String tableName = tableInfo.getTableName();
		String columns = sqlSelectColumns(tableInfo, false);
		String logicDeleteSql = tableInfo.getLogicDeleteSql(true, true);
		if (StringUtil.isNotBlank(logicDeleteSql)) {
			sql += "WHERE 1=1 %s";
		}
		String selectSql = String.format(sql, columns, tableName, logicDeleteSql);
		SqlSource sqlSource = new RawSqlSource(configuration, selectSql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, "selectAll", sqlSource, tableInfo);
	}

}
