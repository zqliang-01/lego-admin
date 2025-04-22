package com.lego.core.data.mybatis.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

public class SelectByCode extends AbstractMethod {

	private static final long serialVersionUID = 1L;

	protected SelectByCode() {
		super("selectByCode");
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "SELECT %s FROM %s WHERE code=#{%s} %s";
        String keyProperty = tableInfo.getKeyProperty();
		String tableName = tableInfo.getTableName();
		String columns = sqlSelectColumns(tableInfo, false);
		String logicDeleteSql = tableInfo.getLogicDeleteSql(true, true);
		String selectSql = String.format(sql, columns, tableName, keyProperty, logicDeleteSql);
		SqlSource sqlSource = new RawSqlSource(configuration, selectSql, String.class);
        return this.addSelectMappedStatementForTable(mapperClass, "selectByCode", sqlSource, tableInfo);
	}
}
