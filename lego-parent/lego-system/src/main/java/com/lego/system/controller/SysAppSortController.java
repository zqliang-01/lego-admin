package com.lego.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysAppInfo;
import com.lego.system.service.ISysAppSortService;
import com.lego.system.service.ISysConfigService;
import com.lego.system.service.ISysPermissionService;
import com.lego.system.vo.SysConfigCode;
import com.lego.system.vo.SysPermissionCode;

@RestController
@RequestMapping("/back-end/sys-app-sort")
public class SysAppSortController extends BaseController {

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private ISysPermissionService permissionService;

	@Autowired
	private ISysAppSortService appSortService;

    @GetMapping("/list-all")
    public JsonResponse<List<SysAppInfo>> listAll() {
    	List<String> validCodes = configService.findListBy(SysConfigCode.APP_VALID_LIST);
    	List<SysAppInfo> validList = new ArrayList<SysAppInfo>();
    	for (SysAppInfo app : permissionService.findAppBy(getLoginCode())) {
    		if (SysPermissionCode.manage.equals(app.getCode())) {
    			continue;
    		}
    		if (validCodes.contains(app.getCode())) {
    			validList.add(app);
    		}
    	}
        return JsonResponse.success(validList);
    }

    @GetMapping("/list-header")
    public JsonResponse<List<SysAppInfo>> listHeader() {
    	List<String> validCodes = configService.findListBy(SysConfigCode.APP_VALID_LIST);
    	List<SysAppInfo> validList = new ArrayList<SysAppInfo>();
    	List<String> permissionCodes = permissionService.findSortAppBy(getLoginCode());
    	for (SysAppInfo app : permissionService.findAppBy(getLoginCode())) {
    		if (SysPermissionCode.manage.equals(app.getCode())) {
    			continue;
    		}
    		if (!permissionCodes.contains(app.getCode())) {
    			continue;
    		}
    		if (validCodes.contains(app.getCode())) {
    			validList.add(app);
    		}
    	}
        return JsonResponse.success(validList);
    }

    @PostMapping("/update")
    public JsonResponse<Object> update(@RequestBody List<String> permissionCodes) {
    	appSortService.update(getLoginCode(), permissionCodes);
        return JsonResponse.success();
    }
}