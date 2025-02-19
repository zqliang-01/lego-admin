package com.lego.system.service.impl;

import com.lego.system.action.ModifySysAppSortAction;
import com.lego.system.service.ISysAppSortService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysAppSortService implements ISysAppSortService {

    @Override
    public void update(String operatorCode, List<String> permissionCodes) {
        new ModifySysAppSortAction(operatorCode, permissionCodes).run();
    }

}
