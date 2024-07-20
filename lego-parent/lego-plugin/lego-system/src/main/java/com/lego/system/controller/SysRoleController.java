package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysRoleInfo;
import com.lego.system.service.ISysRoleService;
import com.lego.system.vo.SysDataScopeAuthVO;
import com.lego.system.vo.SysPermissionAuthVO;
import com.lego.system.vo.SysRoleCreateVO;
import com.lego.system.vo.SysRoleModifyVO;
import com.lego.system.vo.SysRoleSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-role")
public class SysRoleController extends BaseController {

    @Autowired
    private ISysRoleService roleService;

    @PostMapping("/add")
    @SaCheckPermission("manage_role_add")
    public JsonResponse<Object> add(@RequestBody SysRoleCreateVO vo) {
        roleService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify")
    @SaCheckPermission("manage_role_update")
    public JsonResponse<Object> modify(@RequestBody SysRoleModifyVO vo) {
        roleService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/auth")
    @SaCheckPermission("manage_role_auth")
    public JsonResponse<Object> auth(@RequestBody SysPermissionAuthVO vo) {
        roleService.auth(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/auth-data-scope")
    @SaCheckPermission("manage_role_auth")
    public JsonResponse<Object> authDataScope(@RequestBody SysDataScopeAuthVO vo) {
        roleService.authDataScope(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @GetMapping("/list-simple")
    public JsonResponse<List<TypeInfo>> listSimpleType(String name) {
        return JsonResponse.success(roleService.findSimpleType(name, getLoginCode()));
    }

    @GetMapping("/list")
    public JsonResponse<List<SysRoleInfo>> list(SysRoleSearchVO vo) {
        return JsonResponse.success(roleService.findBy(vo));
    }

    @GetMapping("/get/{code}")
    public JsonResponse<SysRoleInfo> get(@PathVariable String code) {
        return JsonResponse.success(roleService.findByCode(code));
    }

    @PostMapping("/delete/{code}")
    @SaCheckPermission("manage_role_delete")
    public JsonResponse<Object> delete(@PathVariable String code) {
        roleService.deleteBy(code);
        return JsonResponse.success();
    }
}
