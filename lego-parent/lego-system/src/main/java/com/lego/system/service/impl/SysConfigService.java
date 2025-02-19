package com.lego.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysConfigDao;
import com.lego.system.dto.SysSystemInfo;
import com.lego.system.entity.SysConfig;
import com.lego.system.service.ISysConfigService;
import com.lego.system.vo.SysConfigCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class SysConfigService implements ISysConfigService {

    @Autowired
    private ISysConfigDao configDao;

    @Override
    public SysSystemInfo findInformation() {
        SysConfig config = configDao.findByCode(SysConfigCode.APP_CONFIG);
        SysSystemInfo sysSystemInfo = JSON.parseObject(config.getValue(), SysSystemInfo.class);
        sysSystemInfo.setVersion(configDao.findValueBy(SysConfigCode.APP_VERSION));
        return sysSystemInfo;
    }

    @Override
    public List<String> findListBy(String code) {
        SysConfig config = configDao.findByCode(code);
        return StrUtil.split(config.getValue(), ",");
    }

    @Override
    public String findValueBy(String code) {
        return configDao.findValueBy(code);
    }

    @Override
    public void update(List<String> value, String code) {
        SysConfig config = configDao.findByCode(code);
        config.setValue(StringUtil.join(new HashSet<String>(value), ","));
        configDao.save(config);
    }

    @Override
    public void update(String value, String code) {
        SysConfig config = configDao.findByCode(code);
        config.setValue(value);
        configDao.save(config);
    }

}
