package com.lego.core.util;

import lombok.SneakyThrows;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class SqlParserUtil {

    @SneakyThrows(JSQLParserException.class)
    public static String addSelectCondition(String sql, String condition) {
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select) {
            Select select = (Select) statement;
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            Expression expression = plainSelect.getWhere();
            Expression envCondition = CCJSqlParserUtil.parseCondExpression(condition);
            if (expression == null) {
                plainSelect.setWhere(envCondition);
            } else {
                AndExpression andExpression = new AndExpression(expression, envCondition);
                plainSelect.setWhere(andExpression);
            }
            return plainSelect.toString();
        }
        return sql;
    }
}
