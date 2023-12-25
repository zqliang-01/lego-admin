package com.lego.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.system.action.ModifySysAppSortAction;
import com.lego.system.service.ISysAppSortService;

@Service
public class SysAppSortService extends BaseService implements ISysAppSortService {

	@Override
	public void update(String operatorCode, List<String> permissionCodes) {
		new ModifySysAppSortAction(operatorCode, permissionCodes).run();
	}

}
