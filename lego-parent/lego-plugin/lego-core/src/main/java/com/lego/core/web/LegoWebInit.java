package com.lego.core.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.InputStream;

@Slf4j
@Component
public class LegoWebInit implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        initFont();
    }

    private void initFont() {
        try (InputStream fontStream = this.getClass().getResourceAsStream("/fonts/simsun.ttf")) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (Exception e) {
            log.error("字体库/fonts/simsun.ttf安装失败，可能会影响到部分字体显示乱码", e);
        }
    }
}
