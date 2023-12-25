package com.lego.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.assembler.SysPermissionAssembler;
import com.lego.system.dto.SysPermissionInfo;
import com.lego.system.service.ISysConfigService;
import com.lego.system.service.ISysPermissionService;
import com.lego.system.vo.SysConfigCode;
import com.lego.system.vo.SysPermissionCreateVO;

@RestController
@RequestMapping("/back-end/sys-permission")
public class SysPermissionController extends BaseController {

	@Autowired
	private ISysPermissionService permissionService;

	@Autowired
	private SysPermissionAssembler permissionAssembler;

	@Autowired
	private ISysConfigService configService;

    @PostMapping("/add")
    public JsonResponse<Object> add(@RequestBody SysPermissionCreateVO vo) {
    	permissionService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @GetMapping("/list-all")
    public JsonResponse<List<SysPermissionInfo>> listAll() {
        return JsonResponse.success(permissionService.findAll());
    }

    @GetMapping("/current")
    public JsonResponse<JSONObject> current() {
    	List<String> validApps = configService.findListBy(SysConfigCode.APP_VALID_LIST);
        List<SysPermissionInfo> permissions = permissionService.findByEmployee(getLoginCode());
		JSONObject auth = permissionAssembler.createAuth(permissions, validApps);
		auth.put("home", "index");
		return JsonResponse.success(auth);
    }

    @GetMapping("/list-menu")
    public JsonResponse<List<SysPermissionInfo>> listMenu() {
        List<SysPermissionInfo> permissions = permissionService.findAllMenu();
		return JsonResponse.success(permissions);
    }

    @GetMapping("/list-role-auth")
    public JsonResponse<List<String>> listRoleAuth(String roleCode) {
		return JsonResponse.success(permissionService.findCodeBy(roleCode));
    }

}
