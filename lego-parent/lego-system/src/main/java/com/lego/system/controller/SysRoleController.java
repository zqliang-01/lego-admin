package com.lego.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysRoleInfo;
import com.lego.system.service.ISysRoleService;
import com.lego.system.vo.SysPermissionAuthVO;
import com.lego.system.vo.SysRoleCreateVO;
import com.lego.system.vo.SysRoleModifyVO;
import com.lego.system.vo.SysRoleSearchVO;

import cn.dev33.satoken.annotation.SaCheckPermission;

@RestController
@RequestMapping("/back-end/sys-role")
public class SysRoleController extends BaseController {

	@Autowired
	private ISysRoleService roleService;

    @PostMapping("/add")
    @SaCheckPermission("manage:role:add")
    public JsonResponse<Object> add(@RequestBody SysRoleCreateVO vo) {
    	roleService.add(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/modify")
    @SaCheckPermission("manage:role:update")
    public JsonResponse<Object> modify(@RequestBody SysRoleModifyVO vo) {
    	roleService.modify(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/auth")
    @SaCheckPermission("manage:role:auth")
    public JsonResponse<Object> auth(@RequestBody SysPermissionAuthVO vo) {
    	roleService.auth(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @GetMapping("/list-simple")
    public JsonResponse<List<TypeInfo>> listSimpleType() {
        return JsonResponse.success(roleService.findSimpleType());
    }

    @GetMapping("/list")
    public JsonResponse<List<SysRoleInfo>> list(SysRoleSearchVO vo) {
        return JsonResponse.success(roleService.findBy(vo));
    }

    @PostMapping("/delete/{code}")
    @SaCheckPermission("manage:role:delete")
    public JsonResponse<Object> delete(@PathVariable String code) {
    	roleService.deleteBy(code);
        return JsonResponse.success();
    }
}
