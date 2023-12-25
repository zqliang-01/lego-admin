package com.lego.core.data.hibernate.jpa;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.exception.CoreException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepositoryFactoryBean<R extends JpaRepository<T, Long>, T> extends JpaRepositoryFactoryBean<R, T, Long> {

	private static final Map<String, Class<?>> daoClassCache = new  ConcurrentHashMap<String, Class<?>>();

    public RepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new BaseRepositoryFactory(em);
    }

    @SuppressWarnings("hiding")
    private class BaseRepositoryFactory<T extends BaseEntity, Long> extends JpaRepositoryFactory {

        public BaseRepositoryFactory(EntityManager em) {
            super(em);
        }

		@Override
        @SuppressWarnings("unchecked")
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        	Class<?> repositoryBaseClass = information.getRepositoryBaseClass();
			GenericDao<T> dao = invokeInstance(repositoryBaseClass);
        	Class<T> domainType = (Class<T>) information.getDomainType();
			dao.init(entityManager, domainType);
			return dao;
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return getDaoImplClass(metadata.getRepositoryInterface().getName());
        }

        private synchronized Class<?> getDaoImplClass(String interfaceName) {
        	if (daoClassCache.containsKey(interfaceName)) {
        		return daoClassCache.get(interfaceName);
        	}
            String implName = interfaceName.substring(interfaceName.lastIndexOf(".") + 2);
            implName = interfaceName.substring(0, interfaceName.lastIndexOf(".")) + ".impl." + implName;
            try {
                Class<?> implClass = Class.forName(implName);
                CoreException.check(GenericDao.class.isAssignableFrom(implClass), "Dao实现类[" + implClass + "]没继承GenericDao");
                daoClassCache.put(interfaceName, implClass);
                return implClass;
            }
            catch (Exception e) {
                throw new CoreException("加载Dao实现类[" + implName + "]出错", e);
            }
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        private GenericDao<T> invokeInstance(Class<?> implClass) {
            try {
            	Constructor c = implClass.getConstructor();
                log.info("Dao实例创建[{}]", implClass);
                return (GenericDao<T>) c.newInstance();
            }
            catch (Exception e) {
                throw new CoreException("初始化Dao实现类[" + implClass + "]出错", e);
            }
        }
    }
}
