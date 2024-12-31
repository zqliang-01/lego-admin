package com.lego.core.data.hibernate.impl;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.data.hibernate.ICommonDao;
import com.lego.core.exception.CoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class CommonDao implements ICommonDao, InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;
    private static CommonDao commonDao;

    @Override
    public Date findCurrentDate() {
        Query query = entityManager.createNativeQuery("SELECT SYSDATE FROM dual");
        return (Date) query.getSingleResult();
    }

    @Override
    public <T extends BaseEntity> T findByCode(Class<T> clazz, String code) {
        T result = findByUnsureCode(clazz, code);
        CoreException.check(result != null, "查询结果不能为空，Code=[{0}]，Type=[{1}]", code, clazz.getSimpleName());
        return result;
    }

    @Override
    public <T extends BaseEntity> T findByUnsureCode(Class<T> clazz, String code) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.where(criteriaBuilder.equal(root.get("code"), code));
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        return uniqueOrNull(query.getResultList());
    }

    @Override
    public <T extends BaseEntity> List<T> findByCodes(Class<T> clazz, List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return new ArrayList<T>();
        }
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("code"));
        criteriaQuery.where(in.value(codes));
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public <T extends BaseEntity> List<T> findAll(Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        Path<T> createTime = root.<T>get("createTime");
        criteriaQuery.orderBy(criteriaBuilder.asc(createTime));
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public static CommonDao getCurrent() {
        return commonDao;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CommonDao.commonDao = this;
    }

    protected static <T> T uniqueOrNull(List<T> objects) {
        int size = objects.size();
        if (size == 0) {
            return null;
        }
        CoreException.check(size == 1, "最多只能返回一条记录，实际返回[{0}]", size);
        return objects.get(0);
    }

    protected void setParameter(Map<String, ?> values, Query query) {
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
}
