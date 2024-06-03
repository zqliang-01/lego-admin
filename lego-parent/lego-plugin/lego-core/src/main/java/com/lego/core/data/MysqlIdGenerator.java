package com.lego.core.data;

import com.lego.core.data.mybatis.mapper.CommonMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MysqlIdGenerator extends IdGenerator implements InitializingBean {

    @Autowired(required = false)
    private CommonMapper commonMapper;

    @Override
    public Long nextId() {
        return commonMapper.getId("general");
    }

    @Override
    public Long nextId(String type) {
        return commonMapper.getId(type);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setIdGenerator(this);
    }

}
