package com.lego.core.data.hibernate.jpa;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.data.hibernate.BusEntity;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.data.hibernate.QueryHandler;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.CoreException;
import com.lego.core.flowable.FlowableCheckStatus;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
public class GenericDao<T extends BaseEntity> extends LegoJpaRepository<T, Long> implements IGenericDao<T> {

    private Class<T> domainClass;
    private EntityManager entityManager;

    protected void init(EntityManager entityManager, Class<T> domainType) {
        this.entityManager = entityManager;
        this.domainClass = domainType;
        this.initJpaRepository(entityManager, domainType);
    }

    protected QueryHandler<T> createQueryHandler() {
        return createQueryHandler("FROM {0} t");
    }

    protected QueryHandler<T> createQueryHandler(String sql) {
        return createQueryHandler(sql, this.domainClass);
    }

    protected <D> QueryHandler<D> createQueryHandler(String sql, Class<D> resultClass) {
        return new QueryHandler<D>(sql, this.entityManager, resultClass);
    }

    protected QueryHandler<T> createQueryHandler(String sql, Class<T> resultClass, Class<?>... domainClass) {
        return new QueryHandler<T>(sql, this.entityManager, resultClass, domainClass);
    }

    @Override
    public List<T> findByCodes(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return new ArrayList<T>();
        }
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);
        Root<T> root = criteriaQuery.from(domainClass);
        CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("code"));
        criteriaQuery.where(in.value(codes));
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public T findByCode(String code) {
        T result = findByUnsureCode(code);
        CoreException.check(result != null, "不存在编码为[{0}]的[{1}]记录！", code, domainClass.getSimpleName());
        return result;
    }

    @Override
    public T findByUnsureCode(String code) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);
        Root<T> root = criteriaQuery.from(domainClass);
        criteriaQuery.where(criteriaBuilder.equal(root.get("code"), code));
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        return EntityUtil.uniqueOrNull(query.getResultList(), domainClass);
    }

    @Override
    public LegoPage<T> findPageBy(GenericConditionVO vo) {
        QueryHandler<T> query = createQueryHandler();
        setGenericParam(vo, query);
        query.order("t.createTime DESC");
        return query.findPage(vo);
    }

    @Override
    public List<T> findBy(GenericConditionVO vo) {
        QueryHandler<T> query = createQueryHandler();
        setGenericParam(vo, query);
        query.order("t.createTime DESC");
        return query.findList();
    }

    @Override
    public long findCountBy(GenericConditionVO vo) {
        QueryHandler<T> query = createQueryHandler();
        setGenericParam(vo, query);
        return query.findCount();
    }

    @Override
    public boolean exists(String code) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(domainClass);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(criteriaBuilder.equal(root.get("code"), code));
        Long result = entityManager.createQuery(criteriaQuery).getSingleResult();
        return result != null && result > 0;
    }

    private void setGenericParam(GenericConditionVO vo, QueryHandler<?> query) {
        if (BusEntity.class.isAssignableFrom(domainClass)) {
            vo.addItem(GenericConditionItemVO.createEqual("checkStatus", FlowableCheckStatus.completed));
        }
        for (GenericConditionItemVO item : vo.getItems()) {
            query.condition(item.getCondition());
            if (item.isNeedValue()) {
                query.param(item.getParamKey(), item.getValue());
            }
        }
        if (StringUtil.isNotBlank(vo.getOrderType())) {
            query.order(vo.getOrderType());
        }
    }

}
