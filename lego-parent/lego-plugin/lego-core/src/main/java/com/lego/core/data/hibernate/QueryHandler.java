package com.lego.core.data.hibernate;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.PageVO;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryHandler<T> extends BaseHandler<T> {

    boolean whereFlag = true;
    boolean orderFlag = true;
    boolean groupFlag = true;
    private String selectSql;
    private StringBuilder joinSqlBuilder;
    private StringBuilder conditionSqlBuilder;
    private StringBuilder orderSqlBuilder;
    private Map<String, Object> params;

    public QueryHandler(String sql, EntityManager em, Class<T> resultClass) {
        this(sql, em, resultClass, resultClass);
    }

    public QueryHandler(String sql, EntityManager em, Class<T> resultClass, Class<?>... domainClasses) {
        super(em, resultClass);
        Object[] classNames = new Object[domainClasses.length];
        for (int i = 0; i < domainClasses.length; ++i) {
            classNames[i] = domainClasses[i].getName();
        }
        this.selectSql = " " + format(sql, classNames);
        this.conditionSqlBuilder = new StringBuilder(" ");
        this.joinSqlBuilder = new StringBuilder(" ");
        this.orderSqlBuilder = new StringBuilder(" ");
    }

    public QueryHandler<T> join(String condition) {
        this.joinSqlBuilder.append(" JOIN ");
        this.joinSqlBuilder.append(condition);
        return this;
    }

    public QueryHandler<T> leftJoin(String condition) {
        this.joinSqlBuilder.append(" LEFT JOIN ");
        this.joinSqlBuilder.append(condition);
        return this;
    }

    public QueryHandler<T> condition(String condition) {
        if (this.whereFlag) {
            this.whereFlag = false;
            this.conditionSqlBuilder.append(" WHERE ");
        } else {
            this.conditionSqlBuilder.append(" AND ");
        }
        this.conditionSqlBuilder.append(condition);
        return this;
    }

    public QueryHandler<T> order(String sqlString) {
        if (this.orderFlag) {
            this.orderFlag = false;
            this.orderSqlBuilder.append(" ORDER BY ");
        } else {
            this.orderSqlBuilder.append(",");
        }
        this.orderSqlBuilder.append(sqlString);
        return this;
    }

    public QueryHandler<T> append(String sqlString) {
        this.conditionSqlBuilder.append(" ");
        this.conditionSqlBuilder.append(sqlString);
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
        List<T> list = limitedFindHQL(this.getSql(), this.params, 0, 1);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<T> limitedFindHQL(String hql, Map<String, ?> values, int firstResult, int maxResults) {
        return super.limitedFindHQL(hql, values, firstResult, maxResults);
    }

    public List<T> findList() {
        return findHQL(getSql(), this.params);
    }

    public List<T> findList(int limit) {
        return limitedFindHQL(getSql(), this.params, 0, limit);
    }

    public List<T> findSqlList() {
        return findSQL(getSql(), this.params, false);
    }

    public List<T> findMap() {
        return findSQL(getSql(), this.params, true);
    }

    public T findUnique() {
        return findUnique(getSql(), this.params);
    }

    public T findSqlUnique() {
        return findSqlUnique(getSql(), this.params);
    }

    public LegoPage<T> findPage(PageVO vo) {
        return findPage(vo.getPageIndex(), vo.getPageSize());
    }

    public LegoPage<T> findPage(int pageIndex, int pageSize) {
        int firstResult = (pageIndex - 1) * pageSize;
        List<T> result = limitedFindHQL(getSql(), this.params, firstResult, pageSize);
        long count = findCount(buildCountString(), this.params);
        return new LegoPage<T>(result, pageIndex, pageSize, count);
    }

    public long findCount() {
        return findCount(buildCountString(), this.params);
    }

    private String buildCountString() {
        String sql = this.getCountSql();
        sql = sql.substring(sql.toUpperCase().indexOf(" FROM "));
        return "SELECT COUNT(1) " + sql;
    }

    public String getSql() {
        return this.selectSql + this.joinSqlBuilder.toString() + this.conditionSqlBuilder.toString() + this.orderSqlBuilder.toString();
    }

    public String getCountSql() {
        return this.selectSql + this.joinSqlBuilder.toString() + this.conditionSqlBuilder.toString();
    }
}
