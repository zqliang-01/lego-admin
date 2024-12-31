package com.lego.core.web;

import cn.hutool.core.io.resource.ResourceUtil;
import com.lego.core.dto.AddressData;
import com.lego.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.InputStream;

@Slf4j
@Component
public class LegoWebInit implements BeanFactoryPostProcessor, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        initFont();
        initData();
    }

    private void initData() {
        InputStream inputStream = ResourceUtil.getStream("classpath:data/address.json");
        AddressData.init(StringUtil.toString(inputStream));
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static int getServerPort() {
        if (applicationContext instanceof ServletWebServerApplicationContext) {
            WebServer webServer = ((ServletWebServerApplicationContext) applicationContext).getWebServer();
            return webServer.getPort();
        }
        return -1;
    }
}
