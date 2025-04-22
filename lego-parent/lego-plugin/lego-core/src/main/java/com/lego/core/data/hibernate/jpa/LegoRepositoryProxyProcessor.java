package com.lego.core.data.hibernate.jpa;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.data.DataPermission;
import com.lego.core.data.ICommonService;
import com.lego.core.web.LegoBeanFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class LegoRepositoryProxyProcessor implements RepositoryProxyPostProcessor {

    private EntityManager entityManager;

    public LegoRepositoryProxyProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
        factory.addAdvice(new MethodInvocationFilter(repositoryInformation));
    }

    public class MethodInvocationFilter implements MethodInterceptor {

        private RepositoryInformation repositoryInformation;

        public MethodInvocationFilter(RepositoryInformation repositoryInformation) {
            this.repositoryInformation = repositoryInformation;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            DataPermission dataPermission = invocation.getMethod().getAnnotation(DataPermission.class);
            if (dataPermission != null) {
                List<String> codes = new ArrayList<>();
                if (dataPermission.type() == DataPermission.Type.EMPLOYEE) {
                    ICommonService commonService = LegoBeanFactory.getBean(ICommonService.class);
                    codes = commonService.findDataPermissionEmployeeCode();
                }
                if (CollectionUtil.isEmpty(codes)) {
                    return invocation.proceed();
                }
                Session session = entityManager.unwrap(Session.class);
                session.enableFilter(dataPermission.type().getName()).setParameterList("filterCodes", codes);
            }
            return invocation.proceed();
        }
    }
}