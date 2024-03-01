package com.lego.core.data.hibernate;

import com.lego.core.exception.CoreException;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class BaseHandler<T> {

    private EntityManager entityManager;
    private Class<T> domainClass;

    protected BaseHandler() {
    }

    public BaseHandler(EntityManager em, Class<T> domainClass) {
        this.entityManager = em;
        this.domainClass = domainClass;
    }

    @SuppressWarnings({"hiding", "unchecked"})
    protected <T> T findUnique(String hql, Map<String, ?> values) {
        return (T) uniqueOrNull(findHQL(hql, values));
    }

    @SuppressWarnings({"unchecked", "hiding"})
    protected <T> T findUnique(String hql, Object... values) {
        return (T) uniqueOrNull(findHQL(hql, values));
    }

    @SuppressWarnings({"unchecked", "hiding"})
    protected <T> T findSqlUnique(String sql, Map<String, Object> values) {
        return (T) uniqueOrNull(findSQL(sql, values, false));
    }

    @SuppressWarnings("unchecked")
    protected List<T> findHQL(String hql, Map<String, ?> values) {
        Query query = entityManager.createQuery(hql);
        setParameter(values, query);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    protected List<T> findHQL(String hql, Object... values) {
        Query query = entityManager.createQuery(hql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    protected List<T> findSQL(String sql, Map<String, Object> parameters, boolean mapResult) {
        Query query = createSqlQuery(sql);
        setParameter(parameters, query);
        if (mapResult) {
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    protected List<T> findSQL(String sql, Object... values) {
        Query query = createSqlQuery(sql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    protected List<T> limitedFindHQL(String hql, Map<String, ?> values, int firstResult, int maxResults) {
        Query query = entityManager.createQuery(hql);
        setParameter(values, query);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    protected List<T> limitedFindHQL(String hql, int firstResult, int maxResults, Object... values) {
        Query query = entityManager.createQuery(hql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    protected List<T> limitedFindSQL(String sql, Map<String, Object> parameters, int firstResult, int maxResults) {
        Query query = createSqlQuery(sql);
        setParameter(parameters, query);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    protected List<T> limitedFindSQL(String sql, int firstResult, int maxResults, Object... values) {
        Query query = createSqlQuery(sql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    protected long findCount(String hql, Map<String, ?> values) {
        Query query = entityManager.createQuery(hql);
        setParameter(values, query);
        return (Long) query.getSingleResult();
    }

    protected long findSqlCount(String sql, Map<String, ?> values) {
        Query query = entityManager.createNativeQuery(sql);
        setParameter(values, query);
        BigDecimal count = (BigDecimal) query.getSingleResult();
        return count.longValue();
    }

    @SuppressWarnings("hiding")
    private <T> T uniqueOrNull(List<T> objects) {
        int size = objects.size();
        CoreException.check(size <= 1, "[{0}]最多允许一个结果, 实际结果数[{1}]", domainClass.getSimpleName(), size);
        if (size == 0) {
            return null;
        }
        return objects.get(0);
    }

    private void setParameter(Map<String, ?> values, Query query) {
        if (values != null) {
            for (Map.Entry<String, ?> entry : values.entrySet()) {
                String paramName = entry.getKey();
                Object paramValue = entry.getValue();
                if (paramName.toLowerCase().endsWith("calendar") || paramValue instanceof Calendar) {
                    query.setParameter(paramName, (Calendar) paramValue, TemporalType.TIMESTAMP);
                } else {
                    query.setParameter(paramName, paramValue);
                }
            }
        }
    }

    protected String format(String pattern, Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }

    private Query createSqlQuery(String sql) {
        if (BaseEntity.class.isAssignableFrom(domainClass)) {
            return entityManager.createNativeQuery(sql, domainClass);
        }
        return entityManager.createNativeQuery(sql);
    }
}