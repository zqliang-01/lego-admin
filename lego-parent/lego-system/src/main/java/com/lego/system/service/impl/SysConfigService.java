package com.lego.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysConfigDao;
import com.lego.system.dto.SysSystemInfo;
import com.lego.system.entity.SysConfig;
import com.lego.system.service.ISysConfigService;
import com.lego.system.vo.SysConfigCode;

@Service
public class SysConfigService extends BaseService implements ISysConfigService {

	@Autowired
	private ISysConfigDao configDao;

	@Override
	public SysSystemInfo findInformation() {
		SysConfig config = configDao.findByCode(SysConfigCode.APP_CONFIG);
		return JSON.parseObject(config.getValue(), SysSystemInfo.class);
	}

	@Override
	public List<String> findListBy(String code) {
		SysConfig config = configDao.findByCode(code);
		if (StringUtil.isNotBlank(config.getValue())) {
			List<String> results = Arrays.asList(config.getValue().split(","));
			return new ArrayList<String>(results);
		}
		return new ArrayList<String>();
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
