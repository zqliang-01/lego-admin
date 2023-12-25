package com.lego.system.service;

import java.util.List;

import com.lego.system.dto.SysSystemInfo;

public interface ISysConfigService {

	SysSystemInfo findInformation();

	List<String> findListBy(String code);

	void update(List<String> value, String code);

	void update(String value, String code);
}
