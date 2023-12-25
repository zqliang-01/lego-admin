package com.lego.core.data.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.PageVO;

public class QueryHandler<T> extends BaseHandler<T> {
    boolean whereFlag = true;
    boolean orderFlag = true;
    boolean groupFlag = true;
    private StringBuilder sqlBuilder;
    private Map<String, Object> params;

    public QueryHandler(String sql, EntityManager em, Class<T> resultClass) {
        super(em, resultClass);
        this.sqlBuilder = new StringBuilder(" ");
        this.sqlBuilder.append(format(sql, new Object[] { resultClass.getName() }));
    }

    public QueryHandler(String sql, EntityManager em, Class<T> resultClass, Class<?>... domainClasses) {
        super(em, resultClass);
        this.sqlBuilder = new StringBuilder(" ");
        Object[] classNames = new Object[domainClasses.length];
        for (int i = 0; i < domainClasses.length; ++i) {
            classNames[i] = domainClasses[i].getName();
        }
        this.sqlBuilder.append(format(sql, classNames));
    }

    public QueryHandler<T> join(String condition) {
        this.sqlBuilder.append(" JOIN ");
        this.sqlBuilder.append(condition);
        return this;
    }

    public QueryHandler<T> condition(String condition) {
        if (this.whereFlag) {
            this.whereFlag = false;
            this.sqlBuilder.append(" WHERE ");
        }
        else {
            this.sqlBuilder.append(" AND ");
        }
        this.sqlBuilder.append(condition);
        return this;
    }

    public QueryHandler<T> order(String sqlString) {
        if (this.orderFlag) {
            this.orderFlag = false;
            append(" ORDER BY ");
        }
        else {
            this.sqlBuilder.append(",");
        }
        this.sqlBuilder.append(sqlString);
        return this;
    }

    public QueryHandler<T> group(String sqlString) {
        if (this.groupFlag) {
            this.groupFlag = false;
            this.sqlBuilder.append(" GROUP BY ");
        }
        else {
            this.sqlBuilder.append(",");
        }
        this.sqlBuilder.append(sqlString);
        return this;
    }

    public QueryHandler<T> append(String sqlString) {
        this.sqlBuilder.append(" ");
        this.sqlBuilder.append(sqlString);
        return this;
    }

    public QueryHandler<T> param(String key, Object value) {
        if (this.params == null) {
            this.params = new HashMap<String, Object>();
        }
        this.params.put(key, value);
        return this;
    }

    public T findFirst() {
        List<T> list = limitedFindHQL(this.sqlBuilder.toString(), this.params, 0, 1);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<T> findList() {
        return findHQL(this.sqlBuilder.toString(), this.params);
    }

    public List<T> findSqlList() {
        return findSQL(this.sqlBuilder.toString(), this.params, false);
    }

    public List<T> findMap() {
        return findSQL(this.sqlBuilder.toString(), this.params, true);
    }

    public T findUnique() {
        return findUnique(this.sqlBuilder.toString(), this.params);
    }

    public T findSqlUnique() {
        return findSqlUnique(this.sqlBuilder.toString(), this.params);
    }

    public LegoPage<T> findPage(PageVO vo) {
        return findPage(vo.getPageIndex(), vo.getPageSize());
    }

    public LegoPage<T> findPage(int pageIndex, int pageSize) {
        int firstResult = (pageIndex - 1) * pageSize;
        List<T> result = limitedFindHQL(this.sqlBuilder.toString(), this.params, firstResult, pageSize);
        long count = findCount(buildCountString(), this.params);
        return new LegoPage<T>(result, pageIndex, pageSize, count);
    }

    public LegoPage<T> findSqlPage(PageVO vo) {
        int firstResult = (vo.getPageIndex() - 1) * vo.getPageSize();
        List<T> result = limitedFindSQL(this.sqlBuilder.toString(), this.params, firstResult, vo.getPageSize());
        long count = findSqlCount(buildSubCountString(), this.params);
        return new LegoPage<T>(result, vo.getPageIndex(), vo.getPageSize(), count);
    }

    public long findCount() {
        return findCount(buildCountString(), this.params);
    }

    private String buildCountString() {
        String sql = this.sqlBuilder.toString();
        sql = sql.substring(sql.toUpperCase().indexOf(" FROM "));
        int orderIndex = sql.toUpperCase().indexOf(" ORDER BY ");
        if (-1 != orderIndex) {
            sql = sql.substring(0, orderIndex);
        }
        return "SELECT COUNT(1) " + sql;
    }

    private String buildSubCountString() {
        String sql = this.sqlBuilder.toString();
        int orderIndex = sql.toUpperCase().indexOf(" ORDER BY ");
        if (-1 != orderIndex) {
            sql = sql.substring(0, orderIndex);
        }
        return "SELECT COUNT(1) FROM (" + sql + ")";
    }

}
