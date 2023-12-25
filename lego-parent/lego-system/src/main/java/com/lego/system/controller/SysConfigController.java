package com.lego.system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysAppInfo;
import com.lego.system.dto.SysSystemInfo;
import com.lego.system.service.ISysConfigService;
import com.lego.system.service.ISysPermissionService;
import com.lego.system.vo.SysConfigCode;

import cn.dev33.satoken.annotation.SaCheckPermission;

@RestController
@RequestMapping("/back-end/sys-config")
public class SysConfigController extends BaseController {

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private ISysPermissionService permissionService;

    @GetMapping("/get-information")
    public JsonResponse<SysSystemInfo> getInformation() {
        return JsonResponse.success(configService.findInformation());
    }

    @PostMapping("/update-information")
    public JsonResponse<Object> updateInformation(String name, String logo) {
    	SysSystemInfo info = configService.findInformation();
    	info.setCompanyName(name);
    	info.setCompanyLogo(logo);
    	configService.update(info.toJson(), SysConfigCode.APP_CONFIG);
        return JsonResponse.success();
    }

    @GetMapping("/list-app")
    @SaCheckPermission("manage:configSet:read")
    public JsonResponse<Map<String, List<SysAppInfo>>> listApp() {
    	List<String> validCodes = configService.findListBy(SysConfigCode.APP_VALID_LIST);
    	Map<String, List<SysAppInfo>> results = new HashedMap<String, List<SysAppInfo>>();
    	List<SysAppInfo> validList = new ArrayList<SysAppInfo>();
    	List<SysAppInfo> inValidList = new ArrayList<SysAppInfo>();
    	for (SysAppInfo app : permissionService.findAllApp()) {
    		if (validCodes.contains(app.getCode())) {
    			validList.add(app);
    			continue;
    		}
    		inValidList.add(app);
    	}
    	results.put("validList", validList);
    	results.put("inValidList", inValidList);
        return JsonResponse.success(results);
    }

    @PostMapping("/set-app")
    @SaCheckPermission("manage:configSet:updated")
    public JsonResponse<Object> setApp(String code, boolean enable) {
    	List<String> validList = configService.findListBy(SysConfigCode.APP_VALID_LIST);
    	if (enable) {
    		validList.add(code);
    		configService.update(validList, SysConfigCode.APP_VALID_LIST);
            return JsonResponse.success();
    	}
    	validList.remove(code);
		configService.update(validList, SysConfigCode.APP_VALID_LIST);
        return JsonResponse.success();
    }

    @GetMapping("/getMessageNum")
    public JsonResponse<JSONObject> getMessageNum() {
    	JSONObject obj = new JSONObject();
    	obj.put("totalNum", 10);
        return JsonResponse.success(obj);
    }

    @GetMapping("/getUnreadMessageNum")
    public JsonResponse<JSONObject> getUnreadMessageNum() {
    	JSONObject obj = new JSONObject();
    	obj.put("allCount", 10);
    	obj.put("announceCount", 10);
    	obj.put("crmCount", 10);
    	obj.put("eventCount", 10);
    	obj.put("examineCount", 10);
    	obj.put("logCount", 10);
    	obj.put("taskCount", 10);
        return JsonResponse.success(obj);
    }
}
