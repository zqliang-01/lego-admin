package com.lego.core.web;

import cn.hutool.core.collection.CollectionUtil;
import com.lego.core.util.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class LegoBeanFactory implements BeanFactoryPostProcessor {

    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        LegoBeanFactory.beanFactory = beanFactory;
    }

    public static <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public static <T> List<T> getBeans(Class<T> clazz) {
        List<T> results = new ArrayList<>();
        Map<String, T> beans = beanFactory.getBeansOfType(clazz);
        if (CollectionUtil.isNotEmpty(beans)) {
            for (T value : beans.values()) {
                results.add(value);
            }
        }
        return results;
    }

    public static <T> T getBeanWithNull(Class<T> clazz) {
        String[] names = beanFactory.getBeanNamesForType(clazz);
        if (StringUtil.isNotBlank(names)) {
            return beanFactory.getBean(clazz);
        }
        return null;
    }

    public static <T> T createBean(Class<T> clazz) {
        if (beanFactory.containsBean(clazz.getName())) {
            return beanFactory.getBean(clazz);
        }
        return beanFactory.createBean(clazz);
    }
}
