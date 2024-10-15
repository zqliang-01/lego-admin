package com.lego.core.web;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.InputStream;

@Component
public class LegoWebInit implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        initFont();
    }

    @SneakyThrows
    private void initFont() {
        InputStream fontStream = this.getClass().getResourceAsStream("/fonts/simsun.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
    }
}
