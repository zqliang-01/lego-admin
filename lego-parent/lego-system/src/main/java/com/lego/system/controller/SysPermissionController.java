package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.fastjson.JSONObject;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.assembler.SysPermissionAssembler;
import com.lego.system.dto.SysPermissionInfo;
import com.lego.system.service.ISysConfigService;
import com.lego.system.service.ISysPermissionService;
import com.lego.system.vo.SysConfigCode;
import com.lego.system.vo.SysPermissionCreateVO;
import com.lego.system.vo.SysPermissionModifyVO;
import com.lego.system.vo.SysPermissionTypeCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @SaCheckPermission("manage_permission_add")
    public JsonResponse<Object> add(@RequestBody SysPermissionCreateVO vo) {
        permissionService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify")
    @SaCheckPermission("manage_permission_update")
    public JsonResponse<Object> modify(@RequestBody SysPermissionModifyVO vo) {
        permissionService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete/{code}")
    @SaCheckPermission("manage_permission_delete")
    public JsonResponse<Object> delete(@PathVariable String code) {
        permissionService.delete(getLoginCode(), code);
        return JsonResponse.success();
    }

    @GetMapping("/list")
    public JsonResponse<List<SysPermissionInfo>> list(String name, String routeType) {
        return JsonResponse.success(permissionService.findBy(name, routeType));
    }

    @GetMapping("/list-type")
    public JsonResponse<List<TypeInfo>> listType() {
        return JsonResponse.success(permissionService.findAllType());
    }

    @GetMapping("/list-route-type")
    public JsonResponse<List<TypeInfo>> listRouteType() {
        return JsonResponse.success(permissionService.findAllRouteType());
    }

    @GetMapping("/get/{code}")
    public JsonResponse<SysPermissionInfo> get(@PathVariable String code) {
        return JsonResponse.success(permissionService.findByCode(code));
    }

    @GetMapping("/list-current")
    public JsonResponse<List<SysPermissionInfo>> listCurrent() {
        List<SysPermissionInfo> permissions = permissionService.findByEmployee(getLoginCode(), SysPermissionTypeCode.SHOW_TYPES);
        return JsonResponse.success(permissions);
    }

    @GetMapping("/current")
    public JsonResponse<JSONObject> current() {
        List<String> validApps = configService.findListBy(SysConfigCode.APP_VALID_LIST);
        List<SysPermissionInfo> permissions = permissionService.findByEmployee(getLoginCode());
        JSONObject auth = permissionAssembler.createAuth(permissions, validApps);
        JSONObject homeAuth = new JSONObject();
        homeAuth.put("code", "home");
        homeAuth.put("title", "首页");
        homeAuth.put("icon", "customer");
        auth.put("home", homeAuth);
        return JsonResponse.success(auth);
    }

    @GetMapping("/list-role-auth")
    public JsonResponse<List<String>> listRoleAuth(String roleCode) {
        return JsonResponse.success(permissionService.findCodeBy(roleCode));
    }

}
